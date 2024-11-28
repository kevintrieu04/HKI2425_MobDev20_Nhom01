package com.example.cinemaapp.network

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cinemaapp.data.Film
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class DbConnect {

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
        "age-rating" to "R",
        "rating" to 8.7,
        "country" to "USA",
        "views" to 1000000,
        "description" to "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
        "img_src" to "https://www.imdb.com/title/tt0133093/mediaviewer/rm4017280512/"
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
                .add(film)
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
                    film?.copy(genre = film.getGenreAsList()) // Chuyển đổi genre về danh sách
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