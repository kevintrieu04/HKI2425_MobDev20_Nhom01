package com.example.cinemaapp.ui

import ActorItem
import com.example.cinemaapp.data.Film
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Actor
import com.example.cinemaapp.data.Comment
import com.example.cinemaapp.network.LoginManager
import com.example.cinemaapp.network.checkRating
import com.example.cinemaapp.network.getCommentsFromFirestore
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
    //sample
    val actors = listOf(
        Actor(
            id = "1",
            name = "Leonardo DiCaprio",
            age = 49,
            bio = "Nam diễn viên người Mỹ nổi tiếng với các vai diễn trong Titanic, Inception và The Revenant.",
            imageUrl = "https://example.com/leonardo.jpg"
        ),
        Actor(
            id = "2",
            name = "Scarlett Johansson",
            age = 39,
            bio = "Nữ diễn viên người Mỹ nổi bật với vai diễn Black Widow trong vũ trụ Marvel.",
            imageUrl = "https://example.com/scarlett.jpg"
        ),
        Actor(
            id = "3",
            name = "Denzel Washington",
            age = 69,
            bio = "Diễn viên và đạo diễn từng đoạt giải, được biết đến qua Training Day và Malcolm X.",
            imageUrl = "https://example.com/denzel.jpg"
        ),
        Actor(
            id = "4",
            name = "Meryl Streep",
            age = 74,
            bio = "Nữ diễn viên gạo cội nổi tiếng với The Devil Wears Prada và Mamma Mia!",
            imageUrl = "https://example.com/meryl.jpg"
        ),
        Actor(
            id = "5",
            name = "Tom Hanks",
            age = 67,
            bio = "Diễn viên đa tài được biết đến qua Forrest Gump, Cast Away và Saving Private Ryan.",
            imageUrl = "https://example.com/tomhanks.jpg"
        )
    )
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
            user?.let { currentUser ->
                firestore.collection("user")
                    .document(currentUser.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            // Lấy thông tin username và profileImage
                            val username = document.getString("userName") ?: "Unknown User"
                            val profileImage = document.getString("profileImage") ?: ""

                            // Tạo dữ liệu bình luận
                            val commentData = hashMapOf(
                                "movieId" to movie.id,
                                "userId" to currentUser.uid,
                                "username" to username,
                                "profileImage" to profileImage,
                                "commentText" to comment,
                                "timestamp" to System.currentTimeMillis()
                            )

                            // Đăng bình luận
                            firestore.collection("comments")
                                .add(commentData)
                                .addOnSuccessListener {
                                    Log.d("Comment", "Comment posted: $commentData")
                                    Toast.makeText(context, "Bình luận thành công", Toast.LENGTH_SHORT).show()
                                    comment = "" // Xóa nội dung sau khi đăng
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Comment", "Error posting comment", e)
                                    Toast.makeText(context, "Lỗi khi đăng bình luận", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Log.e("Comment", "User document does not exist")
                            Toast.makeText(context, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Comment", "Error fetching user info", e)
                        Toast.makeText(context, "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show()
                    }
            } ?: run {
                Log.e("Comment", "User is not logged in")
                Toast.makeText(context, "Vui lòng đăng nhập để bình luận", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("Comment", "Comment is empty")
            Toast.makeText(context, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show()
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
                                        if (manager.isLoggedIn()) {
                                            showPopup = !showPopup
                                        } else {
                                            Toast.makeText(context, "Vui lòng đăng nhập để đánh giá", Toast.LENGTH_SHORT).show()
                                            navController.navigate(AppRouteName.Login)
                                        }
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
                            fontWeight = FontWeight.Bold,
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
                    }
                }

                item {
                    Text(
                        text = "Diễn viên:",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                items(actors) { actor ->
                    ActorItem(actor)
                }

                item {
                    Spacer(modifier = Modifier.width(30.dp).padding(5.dp))
                    Row {
                        Text(
                            text = "Bình luận", style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.height(40.dp)
                                .padding(horizontal = 24.dp)
                                .weight(0.7f)
                        )
                        Button(
                            onClick = { /*AicommentPopup*/ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Nền trong suốt
                                contentColor = Color.Black          // Màu chữ
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            modifier = Modifier
                                .weight(0.13f)
                        ) {
                            Image(
                                painterResource(id = R.drawable.bar_chart),
                                contentDescription = "post",
                                modifier = Modifier.size(40.dp)
                            )
                        }

                    }

                    // Comment input and button
                    if (manager.isLoggedIn()) {
                        Row {
                            Button(
                                onClick = {
                                    checkRating(movie.name) { result ->
                                        if (result) {
                                            postComment()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Vui lòng đánh giá để để bình luận",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .weight(0.3f)
                            ) {
                                Image(
                                    painterResource(id = R.drawable.send_button),
                                    contentDescription = "post",
                                    modifier = Modifier.size(60.dp)
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
                                    .weight(0.7f)
                            )
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