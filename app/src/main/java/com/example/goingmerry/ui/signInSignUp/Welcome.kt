package com.example.goingmerry.ui.signInSignUp

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goingmerry.R

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Banner()

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

            },
            colors = ButtonDefaults
                .buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .height(60.dp)
                .width(295.dp)
        ) {
            Text(text = "Đăng ký")
        }

        Button(
            onClick = {

            },
            colors = ButtonDefaults
                .buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .height(60.dp)
                .width(295.dp)

        ) {
            Text(text = "Đăng nhập")
        }
    }
}


@Composable
fun Banner() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(520.dp)
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
                    .size(240.dp)
                    .clip(CircleShape)
            )

            Column {
                Image(
                    painter = painterResource(R.drawable.party2),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Image(
                    painter = painterResource(R.drawable.party3),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = "Chào mừng bạn đến với Going Merry!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 50.dp)
        )

        Text(
            text = "Nơi chia sẻ, trò chuyện, tán gẫu cùng bạn bè, người thân và gia đình!",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 50.dp)
        )
    }
}