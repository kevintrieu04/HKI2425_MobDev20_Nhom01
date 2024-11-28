package com.example.cinemaapp.ui


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
import com.example.cinemaapp.data.MovieModel

import com.example.cinemaapp.network.LoginManager
import com.example.cinemaapp.ui.navigation.AppRouteName

@Composable
fun DetailScreen(
    navController: NavHostController,
    movie: MovieModel,
) {
    val scrollState = rememberScrollState()
    var comment by remember { mutableStateOf("") }

    val context = LocalContext.current
    val manager = remember {
        LoginManager(context)
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
        LazyColumn {
            // Thêm phần header
            item {
                var showPopup by remember { mutableStateOf(false) }
                var rank by remember { mutableStateOf(0) }
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
//                      .verticalScroll(scrollState) có lỗi
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
                                value = movie.type
                            )
                            MovieInfo(
                                painterResourceId = R.drawable.baseline_access_time_filled,
                                title = "Thời lượng",
                                value = movie.duration
                            )
                            MovieInfo(
                                painterResourceId = R.drawable.baseline_stars,
                                title = "Xếp hạng",
                                value = movie.rating,
                                onClickableNode = {
                                    showPopup = !showPopup
                                }
                            )
                        }
                    }
                    if (showPopup) {
                        StarRatingPopup(
                            movieTitle = movie.title,
                            onDismiss = { showPopup = false },
                            rank = rank,
                            onRatingSelected = { rating ->
                                rank = rating
                                Log.d("RatingPopup", "Đã chọn $rating sao")
                            }
                        )
                    }
                    Text(
                        movie.title, style = MaterialTheme.typography.titleLarge,
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
                        movie.synopsis, style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            horizontal = 24.dp, vertical = 16.dp
                        )
                    )
                    Text(
                        text = "Bình luận", style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.height(40.dp)
                    )
                    if (manager.isLoggedIn()) {
                    Row {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
//                                .padding(horizontal = 24.dp)
                                .weight(0.13f)
                        ) {
                            Image(
                                painterResource(id = R.drawable.send_button),
                                contentDescription = "post",
                                modifier = Modifier.size(40.dp),
//                                contentScale = ContentScale.Fit
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
                    } }
                }

                // Danh sách các bộ phim
            }
            items(sampleComments) { comment ->
                CommentItem(comment)

            }
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .weight(1f)
//                    .padding(8.dp)
//            ) {
//                items(sampleComments) { comment ->
//                    CommentItem(comment)
//
//                }
//            }
        }
//        CommentScreen(sampleComments)
    }
}



@Composable
fun MovieInfo(
    @DrawableRes painterResourceId: Int,
    title: String,
    value: String,
    onClickableNode:   () -> Unit = {}
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

