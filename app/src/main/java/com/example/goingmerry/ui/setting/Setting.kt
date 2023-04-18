package com.example.goingmerry.ui.home

import AccountQuery
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.goingmerry.R
import com.example.goingmerry.ScreenSizes
import com.example.goingmerry.TypeScreen
import com.example.goingmerry.navigate.Routes

@Composable
fun SettingScreen(navController: NavController, name: String, avatar: String, idAccount: String) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(name = name, avatar = avatar)

        BodyScreen(
            navController = navController,
            onNavigateToUserInfo = {
                navController.navigate("userInfo") {
                    launchSingleTop = true
                }
            },
            onNavigateToProfile = {
                navController.navigate(Routes.Profile.route + "/${idAccount}") {
                    launchSingleTop = true
                }
            },
            onNavigateToListAddFriend = {
                navController.navigate(Routes.ListRequestAddFriend.route){
                    launchSingleTop = true
                }
            },
            onNavigateToGroupManager = {
                navController.navigate(Routes.GroupManager.route){
                    launchSingleTop = true
                }
            }
        )
    }

}

@Composable
@Preview
fun PreviewSetting() {
    val navController = rememberNavController()
    SettingScreen(navController = navController, "", "","")
}

@Composable
fun TopBar(
    name: String,
    avatar: String
) {
    var sizeBar = 300.dp
    var fonts = 25.sp
    var sizeImage = 90.dp
    if(ScreenSizes.type() == TypeScreen.Compat){
        sizeBar = 200.dp
        fonts = 20.sp
        sizeImage = 70.dp
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(sizeBar),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(MaterialTheme.colors.primary)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(MaterialTheme.colors.secondary)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "$name",
                fontSize = fonts,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .offset(x = 40.dp)
                    .align(Alignment.BottomStart)
            )
        }
        val imageLoader = ImageLoader(context = LocalContext.current)
        AsyncImage(
            model = avatar,
            imageLoader = imageLoader,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(sizeImage)
                .align(Alignment.CenterHorizontally)
                .offset(x = (-110).dp, y = (-130).dp)
                .clip(CircleShape)
        )
    }
}


@Composable
@Preview
fun PreviewTopBar() {
    TopBar(name = "Lisa", avatar = "0007")
}

@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .clip(CircleShape)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun BodyScreen(
    navController: NavController,
    onNavigateToUserInfo: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToListAddFriend: () -> Unit,
    onNavigateToGroupManager: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        var size  = 40.dp
        var fonts = 25.sp
        var cardHeight = 80.dp
        if(ScreenSizes.type() == TypeScreen.Compat){
            cardHeight = 60.dp
            size = 30.dp
            fonts = 20.sp
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Thẻ thông tin tài khoản
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    onNavigateToUserInfo()
                })
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable._user_info),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Thông tin tài khoản",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Thẻ hồ sơ người dùng
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .clickable(onClick = {
                    onNavigateToProfile()
                })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable._profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Hồ sơ người dùng",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }

        // Thẻ Danh sách bạn bè
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .clickable {
                    onNavigateToListAddFriend()
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable._list_friend),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Danh sách yêu cầu kết bạn",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    onNavigateToGroupManager()
                })
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Quản lý Nhóm",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Thẻ Đăng xuất
        LogoutCard(
            onNavigateToWelcome = {
                navController.navigate("welcome") {
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun LogoutCard(
    onNavigateToWelcome: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    var size  = 40.dp
    var fonts = 25.sp
    var cardHeight = 80.dp
    if(ScreenSizes.type() == TypeScreen.Compat){
        cardHeight = 60.dp
        size = 30.dp
        fonts = 20.sp
    }
    Card(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable(onClick = {
                showDialog.value = true
            })
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable._log_out),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                )

                Spacer(modifier = Modifier.width(30.dp))

                Text(
                    text = "Đăng xuất tài khoản",
                    fontSize = fonts,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Xác nhận đăng xuất") },
                text = { Text(text = "Bạn có chắc chắn muốn đăng xuất?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog.value = false
//                        navController?.navigate(Routes.Welcome.route) {
//                            launchSingleTop = true
//                        }
                        onNavigateToWelcome()
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog.value = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
