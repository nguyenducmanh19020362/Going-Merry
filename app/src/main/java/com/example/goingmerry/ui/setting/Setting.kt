package com.example.goingmerry.ui.home

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes

@Composable
fun SettingScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(name = "Lisa", id = "0007")

        BodyScreen(
            navController = navController,
            onNavigateToUserInfo = {
                navController.navigate("userInfo") {
                    launchSingleTop = true
                }
            },
            onNavigateToProfile = {
                navController.navigate("profile") {
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
    SettingScreen(navController = navController)
}

@Composable
fun TopBar(
    name: String,
    id: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
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
                painter = painterResource(id = R.drawable.cover_image),
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
                text = "$name #$id",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .offset(x = 40.dp)
                    .align(Alignment.BottomStart)
            )
        }

        RoundImage(
            image = painterResource(id = R.drawable.profile_image),
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.CenterHorizontally)
                .offset(x = (-110).dp, y = (-130).dp)
        )
    }
}


@Composable
@Preview
fun PreviewTopBar() {
    TopBar(name = "Lisa", id = "0007")
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
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MaterialTheme.colors.primary)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // Thẻ thông tin tài khoản
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Thông tin tài khoản",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

        // Thẻ hồ sơ người dùng
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Hồ sơ người dùng",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

        // Thẻ Danh sách bạn bè
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = "Danh sách bạn bè",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
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

    Card(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
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
                        .size(40.dp)
                )

                Spacer(modifier = Modifier.width(30.dp))

                Text(
                    text = "Đăng xuất tài khoản",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Image(
                painter = painterResource(id = R.drawable._right_arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
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
