package com.example.cinemaapp.ui

//import com.example.cinemaapp.module.home.model.nowPlayingMovie
//import com.example.cinemaapp.module.home.model.upcoming

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cinemaapp.R
import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.models.DrawerItem
import com.example.cinemaapp.models.drawerItems
import com.example.cinemaapp.network.LoginManager
import com.example.cinemaapp.ui.navigation.AppRouteName
import com.example.cinemaapp.viewmodels.HomePageUiState
import com.example.cinemaapp.viewmodels.HomePageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomePageUiState,
    navController: NavHostController,
    viewModel: HomePageViewModel
) {
    if (uiState is HomePageUiState.Success) {
        val scrollState = rememberScrollState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()


        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerHeader(Modifier.padding(16.dp), navController = navController)
                    DrawerBody(drawerItems, navController) { item ->
                        navController.navigate("${AppRouteName.Drawer}/${item.id}")
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text(
                                "Trang chủ",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(
                            top = padding.calculateTopPadding() + 24.dp,
                            bottom = padding.calculateBottomPadding() + 24.dp,
                        )
                ) {
                    Text(
                        text = "Chào mừng bạn trở lại!",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tìm kiếm hoặc duyệt qua các bộ phim tại đây",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Banners(uiState.ads)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Thể loại",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        TextButton(onClick = {
                            navController.navigate("${AppRouteName.Search}/- Tất cả -")
                        }) {
                            Text(text = "Xem thêm",
                                Modifier.semantics { contentDescription = "Button 1" })
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Categories() { category ->
                        navController.navigate("${AppRouteName.Search}/${category}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Đang chiếu tại rạp",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        TextButton(onClick = {
                            navController.navigate("${AppRouteName.Search}/- Tất cả -")
                        }) {
                            Text(text = "Xem thêm")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingMovie(uiState.movies) { movie ->
                        navController.navigate("${AppRouteName.Detail}/${movie.id}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Tất cả các phim",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        TextButton(onClick = {
                            navController.navigate(AppRouteName.Search)
                        }) {
                            Text(text = "Xem thêm")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    UpcomingMovie(uiState.movies)
                }
            }
        }


    } else if (uiState is HomePageUiState.Loading) {
        LoadingScreen()
    } else {
        ErrorScreen(retryAction = { viewModel.fetchData() })
    }
}

@Composable
fun UpcomingMovie(
    upcoming: List<Film>
) {
    LazyRow(
        contentPadding = PaddingValues(start = 24.dp)
    ) {
        items(count = upcoming.size) { index ->
            Box(modifier = Modifier
                .padding(end = 24.dp)
                .clickable { }
                .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(upcoming[index].imgSrc)
                            .crossfade(true)
                            .build(),
                        error = painterResource(R.drawable.baseline_broken_image_24),
                        contentDescription = "Movie Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.85f)
                            .height(340.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = upcoming[index].name,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlayingMovie(
    nowPlayingMovie: List<Film>,
    onMovieClicked: (Film) -> Unit
) {
    val pagerState =
        rememberPagerState(0, pageCount = { return@rememberPagerState nowPlayingMovie.size })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(start = 48.dp, end = 48.dp)
    ) { page ->

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                    lerp(
                        start = ScaleFactor(1f, 0.85f),
                        stop = ScaleFactor(1f, 1f),
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale.scaleX
                        scaleY = scale.scaleY
                    }
                }
                .clickable {
                    onMovieClicked(nowPlayingMovie[page])
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.BottomCenter

            ) {
                /*Image(
                    painter = painterResource(id = nowPlayingMovie[page].assetImage),
                    contentDescription = "Movie Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.85f)
                        .height(340.dp)
                )*/
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(nowPlayingMovie[page].imgSrc)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.baseline_broken_image_24),
                    contentDescription = "Movie Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.85f)
                        .height(340.dp)
                )
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = (
                                    (pagerState.currentPage - page) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue
                            val translation = pageOffset.coerceIn(0f, 1f)

                            translationY = translation * 200
                        }
                        .fillMaxWidth(fraction = 0.85f)
                        .wrapContentHeight()
                        .background(
                            Color(0xFF427D89)
                        )
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Đặt vé", style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nowPlayingMovie[page].name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Categories(navigateToSearch: (String) -> Unit) {
    val categories = listOf(
        "Hoạt hình",
        "Kinh dị",
        "Hành động",
        "Hài",
        "Lãng mạn",
        "Khoa học viễn tưởng",
        "Lịch sử",
        "Phiêu lưu",
    )
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.horizontalScroll(scrollState)
    ) {
        repeat(categories.size) { index ->
            Surface(
                /// order matters
                modifier = Modifier
                    .padding(
                        start = if (index == 0) 24.dp else 0.dp,
                        end = 12.dp,
                    )
                    .border(width = 1.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {navigateToSearch(categories[index]) }
                    .padding(12.dp)
            ) {
                Text(text = categories[index], style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banners(banners: List<AdModel>) {


    val pagerState = rememberPagerState(0, pageCount = { return@rememberPagerState banners.size })
    val bannerIndex = remember { mutableStateOf(0) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            bannerIndex.value = page
        }
    }

    /// auto scroll
    LaunchedEffect(Unit) {
        while (true) {
            delay(10_000)
            tween<Float>(1500)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % pagerState.pageCount
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .padding(horizontal = 24.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) { index ->
            /*Image(
                painter = painterResource(id = banners[index].id),
                contentDescription = "Banners",
                contentScale = ContentScale.FillBounds,
            )*/
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(banners[index].imgSrc)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.baseline_broken_image_24),
                contentDescription = "Movie Image",
                contentScale = ContentScale.Crop,
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            repeat(banners.size) { index ->
                val height = 12.dp
                val width = if (index == bannerIndex.value) 28.dp else 12.dp
                val color =
                    if (index == bannerIndex.value) MaterialTheme.colorScheme.tertiary else Gray

                Surface(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(width, height)
                        .clip(RoundedCornerShape(20.dp)),
                    color = color,
                ) {
                }
            }
        }
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_wifi_off_24), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current


    val manager = remember {
        LoginManager(context)
    }

    var isLoggedin by remember { mutableStateOf(manager.isLoggedIn()) }

    if (!isLoggedin) {
        Box(modifier.clickable {
            navController.navigate(AppRouteName.Login)
        }) {
            Row {
                Icon(painterResource(R.drawable.baseline_person_24), contentDescription = "")
                Spacer(Modifier.size(5.dp))
                Text(
                    "Bạn chưa đăng nhập",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    } else {
        val user = manager.getUserInfo()
        Box(modifier) {
            Row {
                if (user != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(user.photoUrl)
                            .crossfade(true)
                            .build(),
                        error = painterResource(R.drawable.baseline_broken_image_24),
                        contentDescription = "Movie Image",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            user.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            user.email,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            "Đăng xuất",
                            modifier = Modifier
                                .clickable {
                                    manager.logout()
                                    isLoggedin = false
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerBody(
    drawerItems: List<DrawerItem>,
    navController: NavHostController,
    navigateToScreen: (DrawerItem) -> Unit
) {
    LazyColumn {
        items(drawerItems.size) { index ->
            NavigationDrawerItem(
                label = { Text(drawerItems[index].title) },
                icon = {
                    Icon(
                        drawerItems[index].icon,
                        contentDescription = drawerItems[index].title
                    )
                },
                selected = false,
                onClick = {
                    navigateToScreen(drawerItems[index])
                }
            )
        }
    }
}


