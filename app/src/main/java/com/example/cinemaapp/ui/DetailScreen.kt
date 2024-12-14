package com.example.cinemaapp.ui

import com.example.cinemaapp.data.Film
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Comment
import com.example.cinemaapp.network.LoginManager
import com.example.cinemaapp.ui.navigation.AppRouteName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailScreen(
    navController: NavHostController,
    movie: Film
) {
    val scrollState = rememberScrollState()
    var comment by remember { mutableStateOf("") }
    val context = LocalContext.current
    val manager = remember { LoginManager(context) }
    val firestore = FirebaseFirestore.getInstance()
    val commentsState = remember { mutableStateOf<List<Comment>>(emptyList()) }

    val user = FirebaseAuth.getInstance().currentUser // To check if the user is logged in

    LaunchedEffect(true) {
        Log.d("LaunchedEffect", "running")
        getCommentsFromFirestore().collect { comments ->
            commentsState.value = comments
            Log.d("LaunchedEffect", "comments: ${comments.size}")
        }
    }

    // Function to post the comment to Firebase Firestore
    fun postComment() {
        if (comment.isNotEmpty()) {
            // Lấy thông tin user hiện tại

                            // Tạo dữ liệu bình luận
                            val commentData = hashMapOf(
                                "movieId" to movie.id,
                                "userId" to user?.uid,
                                "commentText" to comment,
                                "timestamp" to System.currentTimeMillis()
                            )

                            // Đăng bình luận
                            firestore.collection("comments")
                                .add(commentData)
                                .addOnSuccessListener {
                                    Log.d("Comment", "Comment posted: $commentData")
                                    comment = "" // Xóa nội dung sau khi đăng
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Comment", "Error posting comment", e)
                                }
                        } else {
                            Log.e("Comment", "User document does not exist")
                        }

    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = RoundedCornerShape(32.dp),
                onClick = {
                    navController.navigate(AppRouteName.SeatSelector)
                },
            ) {
                Text(text = "Đặt vé")
            }
        }
    ) { padding ->
            LazyColumn(Modifier.padding(padding)) {
                // Header and movie details...
                item {
                    var showPopup by remember { mutableStateOf(false) }
                    var rank by remember { mutableStateOf(0) }
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Thông tin phim", style = MaterialTheme.typography.titleLarge)
                        }

                        Row(
                            modifier = Modifier
                                .height(320.dp)
                                .padding(horizontal = 24.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(movie.imgSrc)
                                    .crossfade(true)
                                    .build(),
                                error = painterResource(R.drawable.baseline_broken_image_24),
                                contentDescription = "Poster phim",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .weight(0.7f)
                                    .height(320.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            Column(
                                modifier = Modifier
                                    .height(320.dp)
                                    .weight(0.3f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MovieInfo(
                                    painterResourceId = R.drawable.baseline_videocam,
                                    title = "Thể loại",
                                    value = movie.genre.toString()
                                )
                                MovieInfo(
                                    painterResourceId = R.drawable.baseline_access_time_filled,
                                    title = "Thời lượng",
                                    value = movie.duration.toString() + " phút"
                                )
                                MovieInfo(
                                    painterResourceId = R.drawable.baseline_stars,
                                    title = "Xếp hạng",
                                    value = movie.rating.toString(),
                                    onClickableNode = {
                                        showPopup = !showPopup
                                    }
                                )
                            }
                        }
                        if (showPopup) {
                            StarRatingPopup(
                                movieTitle = movie.name,
                                onDismiss = { showPopup = false },
                                rank = rank,
                                onRatingSelected = { rating ->
                                    rank = rating
                                    Log.d("RatingPopup", "Đã chọn $rating sao")
                                }
                            )
                        }
                        Text(
                            movie.name, style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                horizontal = 24.dp, vertical = 16.dp
                            )
                        )
                        Text(
                            "Tóm tắt", style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(
                                horizontal = 24.dp
                            )
                        )
                        Text(
                            movie.description, style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(
                                horizontal = 24.dp, vertical = 16.dp
                            )
                        )


                        Text(
                            text = "Bình luận", style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.height(40.dp)
                        )

                        // Comment input and button
                        if (manager.isLoggedIn()) {
                            Row {
                                Button(
                                    onClick = { postComment() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .weight(0.13f)
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.send_button),
                                        contentDescription = "post",
                                        modifier = Modifier.size(40.dp)
                                    )
                                }

                                TextField(
                                    value = comment,
                                    onValueChange = { comment = it },
                                    label = { Text("Nhập bình luận") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .weight(0.87f)
                                )
                            }
                        }
                    }
                }

                items(commentsState.value) { comment ->
                    CommentItem(comment)
                }
            }
    }
}


@Composable
fun MovieInfo(
    @DrawableRes painterResourceId: Int,
    title: String,
    value: String,
    onClickableNode: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .border(width = 1.dp, color = Gray, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickableNode() }
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = painterResourceId),
            contentDescription = title,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium)
    }
}