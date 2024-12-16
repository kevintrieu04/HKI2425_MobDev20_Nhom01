package com.example.cinemaapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cinemaapp.R
import com.example.cinemaapp.network.saveRatingToFirestore
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun StarRatingPopup(
    movieTitle: String,
    onDismiss: () -> Unit,
    rank: Int,
    onRatingSelected: (Int) -> Unit,
    homePageViewModel: HomePageViewModel
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .clickable { onDismiss() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
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
                        onClick = {
                            saveRatingToFirestore(movieTitle, rank, context, homePageViewModel)
                            onDismiss()
                        },
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
fun AicommentPopup(
    onDismiss: () -> Unit,
    movieId: String,

) {
    // Trạng thái lưu trữ dữ liệu truy vấn từ Firestore
    val commentState = remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    val firestore = FirebaseFirestore.getInstance()
    // Thực hiện truy vấn Firestore
    LaunchedEffect(movieId) {
        firestore.collection("rateComment")
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val data = documents.documents[0].data as Map<String, Any>
                    // Chuyển dữ liệu thành Map<String, Int> để dễ xử lý
                    val parsedData = data.mapValues { it.value.toString().toIntOrNull() ?: 0 }
                    commentState.value = parsedData
                    Log.d("Firestore",  documents.documents[0].data.toString() )
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents: ${it.message}")
            }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Đánh giá phim",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Hàm phụ hiển thị dòng đánh giá
                @Composable
                fun RatingRow(category: String, goodCount: Int, badCount: Int) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Good",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = goodCount.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Bad",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = badCount.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }

                // Dữ liệu từ commentState
                Log.d("Comment", "Data: ${commentState.value}")
                val data = commentState.value

                // Hiển thị từng danh mục đánh giá
                RatingRow(
                    category = "Hình ảnh",
                    goodCount = data["visual_rate_good_count"] ?: 0,
                    badCount = data["visual_rate_bad_count"] ?: 0
                )
                RatingRow(
                    category = "Diễn xuất",
                    goodCount = data["acter_rate_good_count"] ?: 0,
                    badCount = data["acter_rate_bad_count"] ?: 0
                )
                RatingRow(
                    category = "Nhạc phim",
                    goodCount = data["music_rate_good_count"] ?: 0,
                    badCount = data["music_rate_bad_count"] ?: 0
                )
                RatingRow(
                    category = "Cốt truyện",
                    goodCount = data["storyline_rate_good_count"] ?: 0,
                    badCount = data["storyline_rate_bad_count"] ?: 0
                )
                RatingRow(
                    category = "Đánh giá chung",
                    goodCount = data["category_rate_good_count"] ?: 0,
                    badCount = data["category_rate_bad_count"] ?: 0
                )

                // Nút đóng
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Đóng")
                }
            }
        }
    }
}
