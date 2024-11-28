package com.example.cinemaapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cinemaapp.R

@Composable
fun StarRatingPopup(
    movieTitle: String,
    onDismiss: () -> Unit,
    rank: Int,
    onRatingSelected: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .clickable { onDismiss() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
    //        Card(
    //            modifier = Modifier
    //                .fillMaxWidth(0.8f)
    //                .wrapContentHeight(),
    //            shape = RoundedCornerShape(16.dp),
    //            colors = CardDefaults.cardColors(containerColor = Color.White),
    //            elevation = CardDefaults.cardElevation(8.dp)
    //        ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Đánh giá:",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                            .align(Alignment.Start)
                    )

                    // Hàng nút bấm hình ngôi sao
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = { onRatingSelected(index + 1) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating Star ${index + 1}",
                                    tint = if (index < rank) Color.Black else Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text(text = "Gửi", color = Color.White)
                    }
                }
    //        }
        }
    }
}


@Composable
fun MovieRatingScreen(movieTitle: String) {
    var showPopup by remember { mutableStateOf(false) }
    var rank by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { showPopup = true }) {
            Text(text = "Đánh giá phim")
        }

        if (showPopup) {
            StarRatingPopup(
                movieTitle = movieTitle,
                onDismiss = { showPopup = false },
                rank = rank,
                onRatingSelected = { rating ->
                    showPopup = false
                    rank = rating
                    Log.d("RatingPopup", "Đã chọn $rating sao")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingPopupPreview() {
    var rank by remember { mutableStateOf(0) }
    StarRatingPopup(
        movieTitle = "Interstellar",
        onDismiss = { /* Không làm gì trong preview */ },
        rank = rank,
        onRatingSelected = { rating ->
            rank = rating
            println("Rating selected: $rating") // Chỉ để xem trước, không có logic thực sự
        }
    )
}
