import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Actor

var actor = Actor(
    name = "Keanu Reeves",
    age = 57,
    bio = "Keanu Charles Reeves, sinh ngày 2 tháng 9 năm 1964, là một diễn viên, đạo diễn, nhà sản xuất và nhạc sĩ người Canada.",
//    imageUrl = R.drawable.minion
)

@Composable
fun ActorProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tiêu đề
        Row () {
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

        Divider(
            color = Color.Gray, // Màu của đường thẳng
            thickness = 1.dp,   // Độ dày
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Khoảng cách trên dưới
        )

        // Ảnh diễn viên
        Image(
            painter = painterResource(id = R.drawable.minion), // Thay bằng resource ảnh của bạn
            contentDescription = "Keanu Reeves",
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

@Composable
fun ActorItem(actor: Actor) {
        // Danh sách diễn viên

            Button(
                onClick = {
                    // Hành động khi bấm vào nút
                    /*ActorProfileScreen*/
                },
                modifier = Modifier
                    .padding(vertical = 0.dp),
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

}

//@Preview(showBackground = true)
//@Composable
//fun PreviewActorListLazyColumn() {
//    ActorListLazyColumn()
//}
@Preview(showBackground = true)
@Composable
fun PreviewActorProfileScreen() {
    ActorProfileScreen()
}
