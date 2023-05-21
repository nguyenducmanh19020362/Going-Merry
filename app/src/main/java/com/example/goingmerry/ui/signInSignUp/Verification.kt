package com.example.goingmerry.ui.signInSignUp

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.goingmerry.navigate.Routes
import kotlinx.coroutines.delay
import java.util.Currency
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun VerificationScreen(
    navController: NavController,
    verifyViewModel: VerifyViewModel,
    email: String?,
    typeToken: String?
) {
    val title = "Nhập mã xác thực vừa được gửi tới email $email"

    var verificationCode by rememberSaveable { mutableStateOf("") }
    var buttonOnClick by rememberSaveable { mutableStateOf(false) }
    var errorAuthenCode by rememberSaveable { mutableStateOf(false) }
    val ctx = LocalContext.current

    // Khởi tạo một State để lưu trữ trạng thái của đồng hồ đếm ngược
    val isCountingDown = remember { mutableStateOf(false) }

    // Khởi tạo một State để lưu trữ giá trị của đồng hồ đếm ngược
    val countDownTime = remember { mutableStateOf(2.minutes) }

    // Sự kiện khi nhấn nút Xác thực
    if (buttonOnClick) {
        when {
            email != null && typeToken == "verify-account" -> {
                verifyViewModel.verifyAccount(email = email, token = verificationCode)
                when (verifyViewModel.isVerified.value) {
                    1 -> navController.navigate(Routes.Welcome.route) { launchSingleTop = true }
                    2 -> {
                        isCountingDown.value = true
                        errorAuthenCode = true
                    }
                }
            }
            typeToken == "forgot-password" -> {
                verifyViewModel.exchangeToken(verificationCode)
                navController.navigate(Routes.ChangePassword.route + "/$verificationCode") {
                    launchSingleTop = true
                }
            }

            typeToken == "delete-account" -> {
                verifyViewModel.deleteAccount(verificationCode)
                when (verifyViewModel.isDeleted.value) {
                    1 -> {
                        Toast.makeText(ctx, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.Welcome.route) { launchSingleTop = true }
                    }

                    2 -> {
                        isCountingDown.value = true
                        errorAuthenCode = true
                    }
                }
            }
        }

        buttonOnClick = false
    }

    // Xử lý sự kiện khi người dùng nhấn nút Gửi lại
    val onResendClick = {
        // Đặt giá trị của State để dừng đồng hồ đếm ngược và đặt lại giá trị của nó để bắt đầu một chu kỳ mới
        errorAuthenCode = false
        isCountingDown.value = false
        countDownTime.value = 2.minutes
        isCountingDown.value = true
        verificationCode = ""
        if (email != null) {
            if (typeToken == "verify-account") {
                verifyViewModel.reToken(email = email, per = "VERIFY_ACCOUNT")
            } else if (typeToken == "forgot-password") {
                verifyViewModel.reToken(email = email, per = "FORGET_PASSWORD")
            }
        }
    }

    // Khởi tạo một Coroutine để tính toán và cập nhật giá trị của đồng hồ đếm ngược
    LaunchedEffect(countDownTime.value) {
        while (true) {
            delay(1000)
            countDownTime.value -= 1.seconds

            // Kiểm tra nếu đồng hồ đếm ngược hoàn thành, đặt lại giá trị để bắt đầu một chu kỳ mới
            if (countDownTime.value == Duration.ZERO) {
                countDownTime.value = 2.minutes
            }
        }
    }

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
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 20.dp)
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

        if (errorAuthenCode) {
            Text(
                text = "Mã xác thực không chính xác!",
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 10.dp).width(295.dp),
                color = MaterialTheme.colors.error
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                buttonOnClick = true
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
            modifier = Modifier.padding(bottom = 15.dp).width(295.dp)
        ) {
            Text(
                text = "Chưa nhận được email? ",
                textAlign = TextAlign.Left,
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = " Gửi lại",
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(
                        onClick = onResendClick
                    )
            )
        }

        // Hiển thị đồng hồ đếm ngược nếu đang chạy
        if (isCountingDown.value) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 15.dp)
            ) {
                Text(text = "Mã xác thực làm mới sau: ")

                Text(
                    text = buildAnnotatedString {
                        // Sử dụng format để hiển thị chuỗi dưới dạng mm:ss
                        val countDownString = "%02d:%02d".format(
                            countDownTime.value.toDouble(DurationUnit.MINUTES).toInt(),
                            countDownTime.value.toDouble(DurationUnit.SECONDS).toInt() % 60
                        )

                        // Thêm định dạng cho chuỗi hiển thị của đồng hồ đếm ngược
//                        append("Đồng hồ đếm ngược: ")
                        pushStyle(SpanStyle(color = Color.Red))
                        append(countDownString)
                        pop()
                    }
                )
            }

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



