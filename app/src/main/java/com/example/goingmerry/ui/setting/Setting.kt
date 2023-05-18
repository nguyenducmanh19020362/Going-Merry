package com.example.goingmerry.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import coil.request.ImageRequest
import com.example.goingmerry.*
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.ui.setting.CustomDialog
import com.example.goingmerry.viewModel.AnonymousChatViewModel
import com.example.goingmerry.viewModel.ChatBoxViewModel

@Composable
fun SettingScreen(
    navController: NavController,
    name: String,
    avatar: String,
    idAccount: String,
    data: DataStore,
    chatBoxViewModel: ChatBoxViewModel,
    anonymousChatViewModel: AnonymousChatViewModel,
    token: String
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(name = name, avatar = avatar, token, navController)

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
                navController.navigate(Routes.ListRequestAddFriend.route) {
                    launchSingleTop = true
                }
            },
            onNavigateToGroupManager = {
                navController.navigate(Routes.GroupManager.route) {
                    launchSingleTop = true
                }
            },
            data,
            chatBoxViewModel,
            anonymousChatViewModel
        )
    }

}

@Composable
@Preview
fun PreviewSetting() {
    val navController = rememberNavController()
    //SettingScreen(navController = navController, "", "","")
}

@Composable
fun TopBar(
    name: String,
    avatar: String,
    token: String,
    nav: NavController
) {
    var sizeBar = 300.dp
    var fonts = 25.sp
    var sizeImage = 90.dp
    if (ScreenSizes.type() == TypeScreen.Compat) {
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
                painter = painterResource(id = R.drawable._backgroud),
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
                text = name,
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
            model = ImageRequest.Builder(LocalContext.current)
                .data("${URL.urlServer}${avatar}")
                .setHeader("Authorization", "Bearer $token").build(),
            imageLoader = imageLoader,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(sizeImage)
                .align(Alignment.CenterHorizontally)
                .offset(x = (-110).dp, y = (-130).dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
                .clickable {
                    nav.navigate(Routes.Home.route) {
                        popUpTo(Routes.Setting.route) {
                            inclusive = true
                        }
                    }
                }
        )
    }
}


@Composable
@Preview
fun PreviewTopBar() {
    TopBar(name = "Lisa", avatar = "0007", token = "", nav = NavController(LocalContext.current))
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
    onNavigateToGroupManager: () -> Unit,
    data: DataStore,
    chatBoxViewModel: ChatBoxViewModel,
    anonymousChatViewModel: AnonymousChatViewModel
) {
    var showProgressBar by rememberSaveable {
        mutableStateOf(false)
    }

    if (showProgressBar) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }

    val onClickChangePass = remember { mutableStateOf(false) }
    val onClickDeletedAcc = remember { mutableStateOf(false) }

    if (onClickChangePass.value) {
        CustomDialog(
            title = "Cập nhật mật khẩu",
            onDismiss = { onClickChangePass.value = false }
            )
    }

    if (onClickDeletedAcc.value) {
        CustomDialog(
            title = "Xác nhận xóa tài khoản",
            onDismiss = { onClickDeletedAcc.value = false }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        var size = 30.dp
        var fonts = 20.sp
        var cardHeight = 60.dp
        if (ScreenSizes.type() == TypeScreen.Compat) {
            cardHeight = 60.dp
            size = 30.dp
            fonts = 20.sp
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Thẻ thông tin tài khoản
//        Card(
//            elevation = 4.dp,
//            backgroundColor = MaterialTheme.colors.primary,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(cardHeight)
//                .align(Alignment.CenterHorizontally)
//                .clickable(onClick = {
//                    onNavigateToUserInfo()
//                })
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(12.dp)
//            ) {
//                Row() {
//                    Image(
//                        painter = painterResource(id = R.drawable._user_info),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(size)
//                    )
//
//                    Spacer(modifier = Modifier.width(30.dp))
//
//                    Text(
//                        text = "Thông tin tài khoản",
//                        fontSize = fonts,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//                }
//
//                Image(
//                    painter = painterResource(id = R.drawable._right_arrow),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(size)
//                )
//            }
//        }

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

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                )
            }
        }

        // Thẻ Danh sách yêu cầu kết bạn
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
                        painter = painterResource(id = R.drawable._add_friend),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Yêu cầu kết bạn",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                )
            }
        }

        // Thẻ quản lý nhóm
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
                        painter = painterResource(id = R.drawable._manage_group),
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

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                )
            }
        }

        // Thẻ đổi mật khẩu
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    onClickChangePass.value = true
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
                        painter = painterResource(id = R.drawable._changed_pass_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Đổi mật khẩu",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                )
            }
        }

        // Thẻ Đăng xuất
        LogoutCard(
            onNavigateToWelcome = {
                navController.navigate("welcome") {
                    popUpTo(Routes.Setting.route) {
                        inclusive = true
                    }
                }
            },
            data,
            chatBoxViewModel,
            anonymousChatViewModel,
            changeProgressBar = {
                Log.e("progressBar", it.toString())
                showProgressBar = it
            }
        )

        //Thẻ xóa tài khoản
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    onClickDeletedAcc.value = true
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
                        painter = painterResource(id = R.drawable.deleted_account),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Xóa tài khoản",
                        fontSize = fonts,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                )
            }
        }
    }
}

@Composable
fun LogoutCard(
    onNavigateToWelcome: () -> Unit,
    data: DataStore,
    chatBoxViewModel: ChatBoxViewModel,
    anonymousChatViewModel: AnonymousChatViewModel,
    changeProgressBar: (value: Boolean) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    var clickOk by rememberSaveable { mutableStateOf(false) }
    var exit by rememberSaveable { mutableStateOf(0) }
    if (clickOk) {
        LaunchedEffect(key1 = Unit) {
            chatBoxViewModel.jobReceiver?.cancel()
            chatBoxViewModel.sendJob?.cancel()
            anonymousChatViewModel.jobSend?.cancel()
            anonymousChatViewModel.jobReceiver?.cancel()
            exit = data.saveToken("", 0L)
            clickOk = false
        }
    }
    if (exit == 2) {
        onNavigateToWelcome()
        exit = 0
    }
    if (exit == 1) {
        changeProgressBar(true)
    } else {
        changeProgressBar(false)
    }

    var size = 30.dp
    var fonts = 20.sp
    var cardHeight = 60.dp
    if (ScreenSizes.type() == TypeScreen.Compat) {
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

            Image(
                painter = painterResource(id = R.drawable._right_arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
            )
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Xác nhận đăng xuất") },
                text = { Text(text = "Bạn có chắc chắn muốn đăng xuất?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog.value = false
                        clickOk = true
                        exit = 1
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
