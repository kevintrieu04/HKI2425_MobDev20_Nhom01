package com.example.cinemaapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Ticket

@Composable
fun TicketScreen() {
        val tickets = listOf(
            Ticket(
                movieName = "The Matrix",
                imageUrl = "https://upload.wikimedia.org/wikipedia/en/c/c1/The_Matrix_Poster.jpg",
                seat = "C3, C4, C5, C6",
                time = "10:00",
                date = "20/10/2021"
            ),
            Ticket(
                movieName = "Inception",
                imageUrl = "https://upload.wikimedia.org/wikipedia/en/7/7f/Inception_ver3.jpg",
                seat = "A1, A2, A3",
                time = "15:30",
                date = "22/10/2021"
            ),
            Ticket(
                movieName = "Interstellar",
                imageUrl = "https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg",
                seat = "B4, B5",
                time = "18:45",
                date = "25/10/2021"
            ),
            Ticket(
                movieName = "The Dark Knight",
                imageUrl = "https://upload.wikimedia.org/wikipedia/en/8/8a/Dark_Knight.jpg",
                seat = "D1, D2, D3",
                time = "20:00",
                date = "30/10/2021"
            ),
            Ticket(
                movieName = "Avengers: Endgame",
                imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg",
                seat = "E1, E2, E3, E4",
                time = "14:00",
                date = "01/11/2021"
            )
        )

        // In danh sách các vé
        tickets.forEach { ticket ->
            println("Phim: ${ticket.movieName}")
            println("URL Ảnh: ${ticket.imageUrl}")
            println("Ghế: ${ticket.seat}")
            println("Thời gian: ${ticket.time}")
            println("Ngày: ${ticket.date}")
            println("----------")
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tiêu đề

        // Ảnh diễn viên
        LazyColumn {
            items(tickets) { ticket ->
                Box(modifier = Modifier.padding(8.dp)) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.minion), // Thay bằng resource ảnh của bạn
                            contentDescription = "Keanu Reeves",
                            modifier = Modifier
                                .size(70.dp,120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .align(Alignment.CenterVertically),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ticket.movieName,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 19.sp // Tăng cỡ chữ lên 24sp
                                ),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 0.dp)
                            )


                            Text(
                                text = "Thời gian: " + ticket.time,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 15.sp // Tăng cỡ chữ lên 24sp
                                ),
                                modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp)
                            )

                            Text(
                                text = "Ngày: " + ticket.date,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 15.sp // Tăng cỡ chữ lên 24sp
                                ),
                                modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp)
                            )
                        }

                        Text(
                            text = "x" + ticket.count.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 20.sp // Tăng cỡ chữ lên 24sp
                            ),
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 50.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTicketScreen() {
    TicketScreen()
}