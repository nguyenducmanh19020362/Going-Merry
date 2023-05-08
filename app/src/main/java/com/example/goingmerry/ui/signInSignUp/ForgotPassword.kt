package com.example.goingmerry.ui.signInSignUp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.VerifyViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    verifyViewModel: VerifyViewModel
) {
    var invalidEmailNotification by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    val typeToken = "forgot-password"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Lấy lại mật khẩu",
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(320.dp)
                .padding(bottom = 15.dp)
        )

        Text(
            text = "Nhập email tài khoản của bạn",
            fontSize = 15.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(320.dp)
                .padding(bottom = 15.dp)
        )

        InputTextField(email, onValueChange = { email = it })

        if (invalidEmailNotification) {
            Text(
                text = "Email không hợp lệ",
                modifier = Modifier.padding(bottom = 10.dp),
                color = MaterialTheme.colors.error
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row() {
            Button(
                onClick = {
                    verifyViewModel.forgotPassword(email = email)
                    navController.navigate(
                        route = Routes.Verification.route + "/$email/$typeToken",
                        builder = {
                            launchSingleTop = true
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(140.dp)

            ) {
                Text(text = "Gửi mã ")
            }

            Spacer(modifier = Modifier.width(15.dp))

            Button(
                onClick = {
                navController.navigate(Routes.SignIn.route){
                    launchSingleTop = true
                }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(140.dp)
            ) {
                Text(
                    text = "Hủy",
                    color = Color.Black
                )
            }
        }
    }
}


//@Composable
//@Preview
//fun PreviewForgot() {
//    ForgotPasswordScreen()
//}