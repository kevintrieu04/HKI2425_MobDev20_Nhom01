package com.example.cinemaapp.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.cinemaapp.MainActivity
import com.example.cinemaapp.data.Comment
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.ui.UserProfile
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DatabaseManager {

    private val db = Firebase.firestore

    // Create a new user with a first and last name
    private val person = hashMapOf(
        "name" to "Ada Lovelace",
        "year" to 1815,
        "bio" to "actor"
    )

    private val film = hashMapOf(
        "name" to "The Matrix",
        "year" to 1999,
        "genre" to {"Action"},
        "ageRating" to "R",
        "rating" to 8.7,
        "country" to "USA",
        "views" to 1000000,
        "description" to "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
        "imgSrc" to "https://filmpravda.com/wp-content/uploads/2018/07/the-matrix.jpg"
    )
    private val film1 = hashMapOf(
        "name" to "The Shawshank Redemption",
        "year" to 1994,
        "genre" to {"Drama"},
        "ageRating" to "R",
        "rating" to 9.3,
        "country" to "USA",
        "views" to 20000000,
        "description" to "Two imprisoned ",
        "imgSrc" to "https://upload.wikimedia.org/wikipedia/vi/8/81/ShawshankRedemptionMoviePoster.jpg",
        "isPlaying" to false,
        "duration" to 142
    )
    fun addData() {
        // Add a new document with a generated ID
        db.collection("person")
            .add(person)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun readData() {
        db.collection("person")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun addFilm(): Boolean {
        return try {
            db.collection("film")
                .add(film1)
                .await()
            Log.d(TAG, "Film added successfully")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Error adding film", e)
            false
        }
    }

    // Đọc danh sách các bộ phim từ Firestore
    suspend fun readFilm(): List<Film> {
        return try {
            val snapshot = db.collection("film").get().await()
            snapshot.documents.mapNotNull { document ->
                try {
                    val film = document.toObject(Film::class.java)
                    film?.id = document.id
                    Log.d(TAG, "Film: $film")
                    film?.copy() // Chuyển đổi genre về danh sách
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing document ${document.id}: ${e.message}")
                    null // Bỏ qua các tài liệu không hợp lệ
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading films", e)
            emptyList()
        }
    }


    companion object {
        const val TAG = "DbConnect"
    }
}

fun getCommentsFromFirestore(movieId : String): Flow<List<Comment>> {
    val firestore = FirebaseFirestore.getInstance()
    return callbackFlow {
        val listener = firestore.collection("comments")
            .whereEqualTo("movieId", movieId)
            //  "timestamp")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                snapshot?.let { commentSnapshot ->
                    val comments = mutableListOf<Comment>()
                    val userIDs = commentSnapshot.documents.mapNotNull { document ->
                        document.getString("userId")
                    }.distinct()

                    val userProfiles = mutableMapOf<String, UserProfile>()

                    // Lấy thông tin người dùng trước
                    val userFetchTasks = userIDs.map { userID ->
                        firestore.collection("user")
                            .whereEqualTo("userID", userID) // Truy vấn theo thuộc tính userID
                            .get()
                            .continueWith { task ->
                                if (task.isSuccessful && task.result != null) {
                                    for (document in task.result.documents) { // Có thể có nhiều kết quả, nhưng ở đây ta giả định chỉ có một
                                        val userProfile = document.toObject(UserProfile::class.java)
                                        if (userProfile != null) {
                                            userProfiles[userID] = userProfile
                                            Log.d("UserProfile", userProfile.toString())
                                        }
                                    }
                                } else {
                                    Log.e("UserProfile", "Failed to fetch user for userID: $userID", task.exception)
                                }
                            }
                    }
                    Tasks.whenAll(userFetchTasks).addOnSuccessListener {
                        // Gắn thông tin user vào comment
                        commentSnapshot.documents.forEach { commentDoc ->
                            val comment = commentDoc.toObject(Comment::class.java)
                            if (comment != null) {
                                val userProfile = userProfiles[comment.userId]
                                if (userProfile != null) {
                                    comment.userName = userProfile.username
                                    comment.profileImage = userProfile.imgSrc
                                }
                                comments.add(comment)
                                Log.d("Comment", comment.toString())
                            }
                        }
                        trySend(comments.toList())
                    }.addOnFailureListener { exception ->
                        close(exception)
                    }

                }
            }
        awaitClose { listener.remove() }
    }
}


fun saveRatingToFirestore(filmName: String, rank: Int, context: Context, homePageViewModel: HomePageViewModel) {
    val firestore = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    firestore.collection("ratings")
        .whereEqualTo("user", user?.uid)
        .whereEqualTo("film", filmName)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                val review = hashMapOf(
                    "user" to user?.uid,
                    "film" to filmName,
                    "rank" to rank
                )
                firestore.collection("ratings")
                    .add(review)
                    .addOnSuccessListener {
                        Log.d("RatingPopup", "Đánh giá đã được lưu vào Firestore")
                        Toast.makeText(context, "Đã lưu đánh giá!", Toast.LENGTH_SHORT).show()

                        // Tính và cập nhật rating trung bình sau khi lưu
                        updateFilmRating(filmName, firestore, homePageViewModel)
                    }.addOnFailureListener {
                        Log.e("RatingPopup", "Lỗi khi lưu đánh giá: ${it.message}")
                        Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val review = hashMapOf(
                    "user" to user?.uid,
                    "film" to filmName,
                    "rank" to rank
                )
                firestore.collection("ratings")
                    .document(querySnapshot.documents[0].id)
                    .set(review)
                    .addOnSuccessListener {
                        Log.d("RatingPopup", "Đánh giá đã được cập nhật vào Firestore")
                        Toast.makeText(context, "Đã cập nhật đánh giá!", Toast.LENGTH_SHORT).show()

                        // Tính và cập nhật rating trung bình sau khi cập nhật
                        updateFilmRating(filmName, firestore, homePageViewModel)
                    }.addOnFailureListener {
                        Log.e("RatingPopup", "Lỗi khi cập nhật đánh giá: ${it.message}")
                        Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Log.e("RatingPopup", "Lỗi khi kiểm tra đánh giá: ${it.message}")
            Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
        }
}

private fun updateFilmRating(filmName: String, firestore: FirebaseFirestore ,homePageViewModel: HomePageViewModel) {
    firestore.collection("ratings")
        .whereEqualTo("film", filmName)
        .get()
        .addOnSuccessListener { ratingsSnapshot ->
            val ratings = ratingsSnapshot.documents.mapNotNull { it.getLong("rank")?.toDouble() }

            if (ratings.isNotEmpty()) {
                val averageRating = (ratings.average() * 2).let {
                    String.format("%.1f", it).toDouble()
                }

                firestore.collection("film")
                    .whereEqualTo("name", filmName)
                    .get()
                    .addOnSuccessListener { filmSnapshot ->
                        if (filmSnapshot.documents.isNotEmpty()) {
                            val filmId = filmSnapshot.documents[0].id
                            firestore.collection("film")
                                .document(filmId)
                                .update("rating", averageRating)
                                .addOnSuccessListener {
                                    Log.d("RatingPopup", "Rating trung bình của phim đã được cập nhật: $averageRating")
                                    homePageViewModel.updateRating(filmName, averageRating)
                                }.addOnFailureListener {
                                    Log.e("RatingPopup", "Lỗi khi cập nhật rating phim: ${it.message}")
                                }
                        }
                    }.addOnFailureListener {
                        Log.e("RatingPopup", "Lỗi khi tìm phim: ${it.message}")
                    }
            }
        }.addOnFailureListener {
            Log.e("RatingPopup", "Lỗi khi lấy danh sách đánh giá: ${it.message}")
        }
}
