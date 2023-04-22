package com.example.goingmerry.ui.signInSignUp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goingmerry.DataUserInfo
import com.example.goingmerry.R
import com.example.goingmerry.UserInformation
import com.example.goingmerry.UserInformationDao
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeScreen(navController: NavController, loginViewModel: LoginViewModel,
                  signupViewModel: SignUpViewModel, userInfo: DataUserInfo) {
    val info by userInfo.listInfo.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Banner()

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            colors = ButtonDefaults
                .buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
            modifier = Modifier
                .height(50.dp)
                .width(280.dp),
            onClick = {
                signupViewModel.isSuccessSignUp.value = 1;
                navController.navigate(Routes.SignUp.route){
                    launchSingleTop = true
                }
            },
        ) {
            Text(text = "Đăng ký")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            colors = ButtonDefaults
                .buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .height(50.dp)
                .width(280.dp),
            onClick = {
                //userInfo.getAllInfo()
                Log.e("info", info.size.toString())
                /*if(info.isNotEmpty()){
                    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val ldt = LocalDateTime.parse(info[0].expiredToken, format)
                    Log.e("ldt", ldt.toString())
                    val current = LocalDateTime.parse(Instant.now().toString(), format)
                    Log.e("current", current.toString())
                    if(ldt < current) {
                        loginViewModel.isSuccessLogin.value = 0;
                        navController.navigate(Routes.SignIn.route) {
                            launchSingleTop = true
                        }
                        if(loginViewModel.token.value != "" && loginViewModel.expiredToken.value != ""){
                            userInfo.insert(loginViewModel.token.value, loginViewModel.expiredToken.value)
                        }
                    }else{
                        loginViewModel.token.value = info[0].token.orEmpty()
                        navController.navigate(Routes.Home.route)
                    }
                }else{
                    Log.e("error", "1")
                    loginViewModel.isSuccessLogin.value = 0;
                    navController.navigate(Routes.SignIn.route) {
                        launchSingleTop = true
                    }
                    if(loginViewModel.token.value != "" && loginViewModel.expiredToken.value != ""){
                        userInfo.insert(loginViewModel.token.value, loginViewModel.expiredToken.value)
                    }
                }*/
                loginViewModel.isSuccessLogin.value = 0;
                navController.navigate(Routes.SignIn.route) {
                    launchSingleTop = true
                }
            },

            ) {
            Text(text = "Đăng nhập")
        }
    }
}


@Composable
fun Banner() {
    Column(
        modifier = Modifier
            .height(480.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colors.secondary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()

        Row {
            Image(
                painter = painterResource(R.drawable.party1),
                contentDescription = "",
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
            )

            Column {
                Image(
                    painter = painterResource(R.drawable.party2),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )

                Image(
                    painter = painterResource(R.drawable.party3),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = "Chào mừng bạn đến với Going Merry!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .width(320.dp)
        )

        Text(
            text = "Nơi chia sẻ, trò chuyện, tán gẫu cùng bạn bè, người thân và gia đình!",
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .width(320.dp)
        )
    }
}

/*
@Preview
@Composable
fun ReviewWelcomeScreen(){
    var navController = rememberNavController()
    WelcomeScreen(navController = navController)
}*/
