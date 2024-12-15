package com.example.cinemaapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Comment
import com.example.cinemaapp.ui.navigation.AppRouteName
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserProfile(
    val username: String = "",
    val imgSrc: String = ""
)
fun getCommentsFromFirestore(): Flow<List<Comment>> {
    val firestore = FirebaseFirestore.getInstance()
    return callbackFlow {
        val listener = firestore.collection("comments")
            .orderBy("timestamp")
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




@Composable
fun CommentItem(comment: Comment) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(comment.profileImage),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = comment.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = comment.commentText,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Vừa xong", // Cập nhật thời gian hiển thị
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
fun formatCommentTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60 * 1000 -> "Vừa xong"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} phút trước"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} giờ trước"
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(Date(timestamp))
        }
    }
}
@Preview
@Composable
fun CommentItemPreview() {
    CommentItem(
        Comment(
            userId = "1",
            commentText = "KuMa9B8YbccmUoPjyrjTBPOEDcq2",
            timestamp = System.currentTimeMillis()
        )
    )
}