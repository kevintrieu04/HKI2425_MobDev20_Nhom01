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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.cinemaapp.viewmodels.Film
import com.example.cinemaapp.viewmodels.FilmRepo
import com.example.cinemaapp.viewmodels.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(category: String = "- Tất cả -",
                 viewModel: SearchScreenViewModel) {

    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (category == "- Tất cả -") {
            viewModel.resetSearchState()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Đổi nền thành màu trắng
            .padding(10.dp),
        topBar = {
            // Tiêu đề và thanh tìm kiếm
            SearchBar(
                query = query,
                active = false,
                onQueryChange = {
                    query = it
                    viewModel.updateQuery(name = query)
                                },
                onActiveChange = {/*active = !active*/},
                onSearch = {
                    viewModel.updateQuery(name = query)
                },
                leadingIcon = {Icon(imageVector = Icons.Default.Search, contentDescription = "")},
                placeholder = {Text("Tìm kiếm")},
                modifier = Modifier.padding(horizontal = 20.dp)) {

            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Spacer(modifier = Modifier.height(16.dp))

            // Hàng đầu tiên: Loại phim và Thể loại
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilterDropdown(
                    title = "Loại phim:",
                    options = listOf("- Tất cả -", "Phim Lẻ", "Phim Bộ"),
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterDropdown(
                    title = "Thể loại:",
                    options = listOf(
                        "- Tất cả -",
                        "Hoạt hình",
                        "Kinh dị",
                        "Hành động",
                        "Hài",
                        "Lãng mạn",
                        "Khoa học viễn tưởng",
                        "Lịch sử",
                        "Phiêu lưu",
                        "Tài liệu"),
                    defaultValue = category,
                    viewModel = viewModel
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
                    options = listOf("- Tất cả -", "Việt Nam", "Mỹ", "Hàn Quốc", "Nhật Bản"),
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterDropdown(
                    title = "Năm:",
                    options = listOf("- Tất cả -", "2024", "2023", "2022", "2021"),
                    viewModel = viewModel
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
                    options = listOf("- Tất cả -", "Dưới 1 giờ", "1-2 giờ", "Trên 2 giờ"),
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterDropdown(
                    title = "Sắp xếp:",
                    options = listOf("Ngày cập nhật", "Tên A-Z", "Tên Z-A", "Điểm cao nhất"),
                    viewModel = viewModel
                )
            }
            FilmList(viewModel)
        }

    }


}

@Composable
fun FilterDropdown(title: String, options: List<String>,
                   defaultValue: String = "- Tất cả -",
                   viewModel: SearchScreenViewModel) {
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
                        when (title) {
                            "Loại phim:" -> viewModel.updateQuery(type = option)
                            "Thể loại:" -> viewModel.updateQuery(category = option)
                            "Quốc gia:" -> viewModel.updateQuery(country = option)
                            "Năm:" -> viewModel.updateQuery(year = option)
                            "Thời lượng:" -> viewModel.updateQuery(duration = option)
                            "Sắp xếp:" -> viewModel.updateQuery(sort = option)
                        }
                        expanded = false
                    }
                )
            }
        }


    }
}

@Composable
fun FilmList(viewModel: SearchScreenViewModel) {

    val searchList by viewModel.searchList.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(searchList) { film ->
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


@Preview(showBackground = true)
@Composable
fun PreviewFilterScreen() {
    SearchScreen(viewModel = SearchScreenViewModel())
}
