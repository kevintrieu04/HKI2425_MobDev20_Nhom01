import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Actor

@Composable
fun ActorProfileScreen(actor: Actor, onClose: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nút đóng popup
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Đóng",
                        tint = Color.Gray
                    )
                }
            }

            // Tiêu đề
            Row {
                Text(
                    text = actor.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Age: ${actor.age}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Gray
            )

            // Ảnh diễn viên
            Image(
                painter = rememberAsyncImagePainter(actor.img_src),
                contentDescription = actor.name,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thông tin chi tiết
            Text(
                text = actor.bio,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ActorItem(actor: Actor) {
    var showPopup by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = {
                showPopup = true
            },
            modifier = Modifier.padding(vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, // Nền trong suốt
                contentColor = Color.Black          // Màu chữ
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp) // Loại bỏ hiệu ứng nổi
        ) {
            Text(
                text = actor.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Hiển thị popup khi trạng thái `showPopup` là true
        if (showPopup) {
            Dialog(onDismissRequest = { showPopup = false }) {
                ActorProfileScreen(actor = actor, onClose = { showPopup = false })
            }
        }
    }
}

