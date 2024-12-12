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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class UserProfile(
    val username: String = "",
    val imgSrc: String = "", // Địa chỉ URL của ảnh đại diện người dùng
    val userID: String = ""
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
                snapshot?.let {
                    val comments = it.documents.mapNotNull { document ->
                        document.toObject(Comment::class.java)
                    }
                    trySend(comments)
                    Log.d("CommentScreen", "getCommentsFromFirestore: $comments")
                }
            }
        awaitClose { listener.remove() }
    }
}

fun getUserInfo(userID: String, onComplete: (List<UserProfile>) -> Unit) {
    FirebaseFirestore.getInstance().collection("user")
        .whereEqualTo("userID", userID) // Lọc các document có trường userID trùng khớp
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val users = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(UserProfile::class.java)
                }
                Log.d("Firestore", "User info: $users")
                onComplete(users)
            } else {
                onComplete(emptyList()) // Không tìm thấy kết quả
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error getting user info", e)
            onComplete(emptyList())
        }
}

@Composable
fun CommentScreen() {
    val commentsState = remember { mutableStateOf<List<Comment>>(emptyList()) }
    val userProfilesState = remember { mutableStateOf<Map<String, UserProfile>>(emptyMap()) }
    Log.d("CommentScreen", "CommentScreen")

    // Lấy dữ liệu bình luận từ Firestore
    LaunchedEffect(true) {
        getCommentsFromFirestore().collect { comments ->
            commentsState.value = comments

            // Lấy thông tin người dùng cho từng userID trong các bình luận
            val userIDs = comments.map { it.userId }.distinct()
            val userProfiles = mutableMapOf<String, UserProfile>()
            Log.d("CommentScreen", "UserIDs: $userIDs")
            userIDs.forEach { userID ->
                getUserInfo(userID) { userProfile ->
                    if (userProfile.isNotEmpty()) {
                        userProfiles[userID] = userProfile.firstOrNull() ?: return@getUserInfo
                        userProfilesState.value = userProfiles.toMap()
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(commentsState.value) { comment ->
                val userProfile = userProfilesState.value[comment.userId]
                if (userProfile != null) {
                    CommentItem(comment, userProfile)
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment, userProfile: UserProfile) {
    Log.d("CommentScreen", "CommentItem: $comment")
    Row(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(userProfile.imgSrc)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.baseline_broken_image_24),
            contentDescription = "User Image",
            contentScale = ContentScale.Crop,
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
                    text = userProfile.username,
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
                        text = "Vừa xong",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}