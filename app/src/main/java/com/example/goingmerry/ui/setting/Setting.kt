package com.example.goingmerry.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.goingmerry.*
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.ui.signInSignUp.InputPasswordField
import com.example.goingmerry.ui.signInSignUp.InputRePasswordField
import com.example.goingmerry.viewModel.AnonymousChatViewModel
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.VerifyViewModel

@Composable
fun SettingScreen(
    navController: NavController,
    name: String,
    avatar: String,
    idAccount: String,
    data: DataStore,
    chatBoxViewModel: ChatBoxViewModel,
    anonymousChatViewModel: AnonymousChatViewModel,
    token: String,
    verifyViewModel: VerifyViewModel,
    loginViewModel: LoginViewModel
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
            anonymousChatViewModel,
            verifyViewModel,
            loginViewModel
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
    anonymousChatViewModel: AnonymousChatViewModel,
    verifyViewModel: VerifyViewModel,
    loginViewModel: LoginViewModel
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

    var password by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var rePassword by rememberSaveable { mutableStateOf("") }
    var wrongRePW by rememberSaveable { mutableStateOf(false) }
    var wrongPW by rememberSaveable { mutableStateOf(false) }
    val ctx = LocalContext.current
    val typeToken = "delete-account"
    val email = "của bạn"

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
//        ChangePWCard()

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

    if (onClickChangePass.value) {
        Dialog(
            onDismissRequest = { onClickChangePass.value = false }
        ) {
            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable(
                                onClick = { onClickChangePass.value = false }
                            )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Cập nhật mật khẩu",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nhập mật khẩu hiện tại và mật khẩu mới",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Mật khẩu hiển tại",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .width(295.dp)
                                .padding(bottom = 5.dp)
                        )

                        InputPasswordField(password, onValueChange = { password = it })

                        Spacer(modifier = Modifier.height(5.dp))

                        if (wrongRePW) {
                            Text(
                                text = "Password nhập lại không đúng",
                                modifier = Modifier.padding(bottom = 5.dp),
                                color = MaterialTheme.colors.error
                            )
                        }

                        Text(
                            text = "Mật khẩu mới",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .width(295.dp)
                                .padding(bottom = 5.dp)
                        )

                        InputPasswordField(newPassword, onValueChange = { newPassword = it })

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Nhập lại mật khẩu mới",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .width(295.dp)
                                .padding(bottom = 5.dp)
                        )

                        InputRePasswordField(rePassword, onValueChange = { rePassword = it })

                        Spacer(modifier = Modifier.height(10.dp))

                        if (wrongRePW) {
                            Text(
                                text = "Password nhập lại không đúng",
                                modifier = Modifier.padding(bottom = 5.dp),
                                color = MaterialTheme.colors.error
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { onClickChangePass.value = false }
                            ) {
                                Text(text = "Hủy")
                            }

                            Button(
                                onClick = {
                                    if (rePassword != newPassword) {
                                        wrongRePW = true
                                    } else {
                                        verifyViewModel.changePassword(loginViewModel.token.value, password, newPassword)
                                        when (verifyViewModel.isVerified.value) {
                                            1 -> {
                                                Toast.makeText(ctx, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                                                onClickChangePass.value = false
                                            }

                                            2 -> Toast.makeText(ctx, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
                                        }

                                    }

                                }
                            ) {
                                Text(text = "Xong")
                            }
                        }
                    }
                }
            }
        }
    }

    if (onClickDeletedAcc.value) {
        Dialog(onDismissRequest = { onClickDeletedAcc.value = false }) {
            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable(
                                onClick = { onClickDeletedAcc.value = false }
                            )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Xác nhận xóa tài khoản",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nhập mật khẩu hiện tại để xác nhận xóa tài khoản",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Mật khẩu hiển tại",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .width(295.dp)
                                .padding(bottom = 5.dp)
                        )

                        InputPasswordField(password, onValueChange = { password = it })

                        if (wrongPW) {
                            Text(
                                text = "Password nhập lại không đúng",
                                modifier = Modifier.padding(top = 5.dp),
                                color = MaterialTheme.colors.error
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { onClickDeletedAcc.value = false }
                            ) {
                                Text(text = "Hủy")
                            }

                            Button(
                                onClick = {
                                    verifyViewModel.reqDelAcc(password)
                                    when (verifyViewModel.isReqDel.value) {
                                        1 -> {
                                            navController.navigate(Routes.Verification.route + "/$email/$typeToken")
                                            { launchSingleTop = true }
                                        }

                                        2 -> wrongPW = true
                                    }

                                }
                            ) {
                                Text(text = "Xong")
                            }
                        }
                    }
                }
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
