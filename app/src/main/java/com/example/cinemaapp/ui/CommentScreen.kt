package com.example.cinemaapp.ui

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

data class Comment(
    val userName: String,
    val content: String,
    val timeAgo: String,
    val reactions: String,
    val profileImage: Int // Giả định đây là resource ID của ảnh đại diện
)
val sampleComments = listOf(
    Comment("Minh Hoàng", "Phim này đúng là kiệt tác của điện ảnh, không thể rời mắt được! 🎬🔥", "2 ngày", "15 👍😍", R.drawable.user),
    Comment("Lan Phương", "Kết thúc phim làm mình ngạc nhiên quá, không ngờ lại như vậy! 😮👏", "3 ngày", "23 😂👍", R.drawable.user),
    Comment("Trung Kiên", "Diễn xuất của diễn viên chính rất xuất sắc, cảm xúc dâng trào! 👏👏", "1 ngày", "18 ❤️👍", R.drawable.user),
    Comment("Hà Vy", "Cốt truyện rất mới lạ, nhưng một số chi tiết hơi khó hiểu. Ai giải thích giúp mình được không? 🤔", "5 giờ", "9 🤔😂", R.drawable.user),
    Comment("Bảo Anh", "Nhạc phim đỉnh quá, nghe hoài không chán! 🎶😍", "6 giờ", "20 ❤️🔥", R.drawable.user),
    Comment("Quốc Bảo", "Mong sẽ có phần tiếp theo vì kết thúc mở quá, không thể chờ thêm! 😭🙏", "1 ngày", "30 😭👍", R.drawable.user),
    Comment("Thanh Thảo", "Kỹ xảo và hình ảnh đẹp mắt, xứng đáng là phim bom tấn! 🤩👏", "3 ngày", "12 👍🤩", R.drawable.user),
    Comment("Hữu Tài", "Xem phim xong mà cứ nghĩ mãi, đúng là tác phẩm để đời. 📽️✨", "12 giờ", "14 ❤️🔥", R.drawable.user)
)

@Composable
fun CommentScreen(comments: List<Comment>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        items(comments) { comment ->
            CommentItem(comment)

        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(modifier = Modifier.padding(8.dp)) {
        // Thêm hình ảnh đại diện nằm ngoài phần nền
        Image(
            painter = painterResource(id = comment.profileImage),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape), // Bo góc tròn cho avatar
            contentScale = ContentScale.Crop
        )

        // Card chứa phần nội dung bình luận
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
                    text = comment.content,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = comment.timeAgo,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = comment.reactions,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AicommentPopup(
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .clickable { onDismiss() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Dòng 0
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tính chất",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = "Star Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dislike),
                            contentDescription = "Star Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Dòng 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Hình ảnh",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "6", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "9", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Dòng 2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Diễn viên",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "3", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "5", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Dòng 3
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Âm nhạc",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "5", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "9", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Dòng 4
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Cốt truyện",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "6", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "2", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Dòng 5
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tổng quan",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "10", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "2", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}




@Preview
@Composable
fun CommentItemPreview() {
    AicommentPopup(onDismiss = { /* Không làm gì trong preview */ },)
}


// Hàm gọi để hiển thị màn hình
@Composable
fun MainScreen() {
    CommentScreen(comments = sampleComments)
}


//@Preview(showBackground = true)
//@Composable
//fun CommentScreenPreview() {
//    MainScreen()
//}