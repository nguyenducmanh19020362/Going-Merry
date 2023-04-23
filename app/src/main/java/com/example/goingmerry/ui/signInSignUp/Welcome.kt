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
import com.example.goingmerry.DataStore
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel
import com.example.goingmerry.viewModel.StartScreenViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeScreen(navController: NavController, loginViewModel: LoginViewModel,
                  signupViewModel: SignUpViewModel, startScreenViewModel: StartScreenViewModel, dataStore: DataStore
) {
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
                startScreenViewModel.setLogin(dataStore, loginViewModel)
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
