package com.example.goingmerry.ui.signInSignUp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.goingmerry.navigate.Routes

@Composable
fun VerificationScreen(
    navController: NavController,
    titlee: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val email = navBackStackEntry?.arguments?.getString("email")
    val titlee = "Nhập mã xác thực vừa được gửi tới email $email của bạn"
    val numberOfBoxes = 4 // số lượng ô vuông
    val boxSize = 48.dp // kích thước của mỗi ô vuông
    val spacing = 8.dp // khoảng cách giữa các ô vuông

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = titlee,
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 1..4) {
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(60.dp)
                        .background(Color.White)
                        .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { /* Move focus to the next text field */ }
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
//                navController.navigate(Routes.Home.route){
//                    launchSingleTop = true
//                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .height(60.dp)
                .width(160.dp)
                .padding(bottom = 15.dp)

        ) {
            Text(text = "Xác thực")
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 15.dp)
        ) {
            Text(text = "Chưa nhận được email?")

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Gửi lại",
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