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

data class UserProfile(
    val userName: String,
    val profileImage: String // Địa chỉ URL của ảnh đại diện người dùng
)



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
