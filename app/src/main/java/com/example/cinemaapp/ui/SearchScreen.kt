import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinemaapp.R

@Composable
fun SearchSceen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Đổi nền thành màu trắng
            .padding(10.dp)
    ) {
        // Tiêu đề và thanh tìm kiếm
        Header()

        Spacer(modifier = Modifier.height(16.dp))

        // Hàng đầu tiên: Loại phim và Thể loại
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterDropdown(
                title = "Loại phim:",
                options = listOf("- Tất cả -", "Phim Lẻ", "Phim Bộ")
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterDropdown(
                title = "Thể loại:",
                options = listOf("- Tất cả -", "Hành động", "Hài", "Tình cảm", "Kinh dị")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hàng thứ hai: Quốc gia và Năm
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterDropdown(
                title = "Quốc gia:",
                options = listOf("- Tất cả -", "Việt Nam", "Mỹ", "Hàn Quốc", "Nhật Bản")
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterDropdown(
                title = "Năm:",
                options = listOf("- Tất cả -", "2024", "2023", "2022", "2021")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hàng thứ ba: Thời lượng và Sắp xếp
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterDropdown(
                title = "Thời lượng:",
                options = listOf("- Tất cả -", "Dưới 1 giờ", "1-2 giờ", "Trên 2 giờ")
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterDropdown(
                title = "Sắp xếp:",
                options = listOf("Ngày cập nhật", "Tên A-Z", "Tên Z-A", "Điểm cao nhất")
            )
        }
        FilmList()
    }


}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Biểu tượng menu
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.Black, // Đổi màu icon thành đen
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Tên ứng dụng
        Text(
            text = "",
            fontSize = 24.sp,
            color = Color(0xFF007ACC), // Màu xanh dương nhẹ
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f) // Đẩy thanh tìm kiếm sang phải
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Thanh tìm kiếm
        Row(
            modifier = Modifier
                .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp)) // Màu xám nhạt
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black, // Đổi icon tìm kiếm thành màu đen
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tìm kiếm",
                color = Color.DarkGray, // Chữ xám đậm
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun FilterDropdown(title: String, options: List<String>, defaultValue: String = "- Tất cả -") {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultValue) }

    Column(modifier = Modifier.width(150.dp)) {
        // Tiêu đề của dropdown
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black, // Chữ đen cho dễ đọc
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Nút bấm để mở dropdown menu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp)) // Nền xám nhạt
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = selectedOption,
                color = Color.Black, // Chữ đen
                textAlign = TextAlign.Center
            )
        }

        // DropdownMenu hiển thị danh sách lựa chọn
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(150.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(option, color = Color.Black) }, // Chữ trong menu là đen
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }


    }
}

@Composable
fun FilmList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(FilmRepo.filmList) { film ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.White)
                    .padding(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = film.imgSrc.toInt()),
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = film.name,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = film.description,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

object FilmRepo {
    val filmList = listOf(
        Film(
            key = "1",
            name = "Film 1",
            year = 2021,
            genre = "Action",
            ageRating = "PG-13",
            rating = 8.5,
            country = "USA",
            views = 1000,
            description = "Description 1"
        ),
        Film(
            key = "2",
            name = "Film 2",
            year = 2020,
            genre = "Comedy",
            ageRating = "PG",
            rating = 7.2,
            country = "UK",
            views = 1500,
            description = "Description 2"
        ),
        Film(
            key = "3",
            name = "Film 3",
            year = 2019,
            genre = "Drama",
            ageRating = "R",
            rating = 9.0,
            country = "France",
            views = 2000,
            description = "Description 3"
        ),
        Film(
            key = "4",
            name = "Film 4",
            year = 2018,
            genre = "Horror",
            ageRating = "PG-13",
            rating = 6.8,
            country = "Japan",
            views = 2500,
            description = "Description 4"
        ),
        Film(
            key = "5",
            name = "Film 5",
            year = 2017,
            genre = "Sci-Fi",
            ageRating = "PG",
            rating = 8.0,
            country = "Canada",
            views = 3000,
            description = "Description 5"
        ),
        Film(
            key = "6",
            name = "Film 6",
            year = 2016,
            genre = "Romance",
            ageRating = "PG-13",
            rating = 7.5,
            country = "India",
            views = 3500,
            description = "Description 6"
        ),
        Film(
            key = "7",
            name = "Film 7",
            year = 2015,
            genre = "Thriller",
            ageRating = "R",
            rating = 8.3,
            country = "Germany",
            views = 4000,
            description = "Description 7"
        ),
        Film(
            key = "8",
            name = "Film 8",
            year = 2014,
            genre = "Animation",
            ageRating = "G",
            rating = 9.1,
            country = "South Korea",
            views = 4500,
            description = "Description 8"
        ),
        Film(
            key = "9",
            name = "Film 9",
            year = 2013,
            genre = "Fantasy",
            ageRating = "PG",
            rating = 7.9,
            country = "Australia",
            views = 5000,
            description = "Description 9"
        ),
        Film(
            key = "10",
            name = "Film 10",
            year = 2012,
            genre = "Documentary",
            ageRating = "PG",
            rating = 8.7,
            country = "Brazil",
            views = 5500,
            description = "Description 10"
        )
    )
}


data class Film(
    val key: String = "",
    val name: String = "",
    val year: Int = 0,
    val genre: String = "",
    val ageRating: String = "",
    val rating: Double = 0.0,
    val country: String = "",
    val views: Int = 0,
    val description: String = "",
    val imgSrc: String = R.drawable.minion.toString()
)





@Preview(showBackground = true)
@Composable
fun PreviewFilterScreen() {
    SearchSceen()
}
