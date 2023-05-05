package com.example.goingmerry.ui.signInSignUp

import com.example.goingmerry.viewModel.VerifyViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.goingmerry.navigate.Routes

@Composable
fun VerificationScreen(
    navController: NavController,
    verifyViewModel: VerifyViewModel,
    email: String?
//    isForgotPassword: Boolean = false
) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val email = navBackStackEntry?.arguments?.getString("email")
    val title = "Nhập mã xác thực vừa được gửi tới email $email"
    var verificationCode by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = title,
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(300.dp).padding(bottom = 20.dp)
        )

        TextField(
            value = verificationCode,
            onValueChange = { verificationCode = it },
            singleLine = true,
            modifier = Modifier
                .height(60.dp)
                .width(295.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.secondaryVariant),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (email != null) {
                    verifyViewModel.verifyAccount(email = email, token = verificationCode)
                }
                navController.navigate(Routes.Welcome.route){
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .padding(bottom = 15.dp)
                .height(60.dp)
                .width(295.dp)
        ) {
            Text(text = "Xác thực")
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 15.dp)
        ) {
            Text(text = "Chưa nhận được email? ")

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = " Gửi lại",
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(onClick = {

                    })
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clickable(onClick = {
                    navController.navigate(Routes.SignIn.route) {
                        launchSingleTop = true
                    }
                })
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Quay trở lại đăng nhập",
                color = Color.Blue
            )
        }

    }
}

//@Composable
//@Preview
//fun PreviewVerification() {
//    VerificationScreen(
//        titlee = "Nhập mã xác thực vừa được gửi tới email của bạn"
//    )
//}