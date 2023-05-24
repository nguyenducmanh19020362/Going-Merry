package com.example.goingmerry.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.goingmerry.ui.signInSignUp.InputPasswordField
import com.example.goingmerry.ui.signInSignUp.InputRePasswordField
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun CustomDialog(
    title: String?,
    onDismiss: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var rePassword by rememberSaveable { mutableStateOf("") }
    var wrongPW by rememberSaveable { mutableStateOf(false) }
    var wrongRePW by rememberSaveable { mutableStateOf(false) }
    val showVerifyDialog = remember { mutableStateOf(false) }
    var verificationCode by rememberSaveable { mutableStateOf("") }
    var errorAuthenCode by rememberSaveable { mutableStateOf(false) }

//    if (showVerifyDialog.value) {
//        CustomDialog(
//            title = "Xác thực xóa tài khoản",
//            onDismiss = { !showVerifyDialog.value }
//        )
//    }

    // Khởi tạo một State để lưu trữ trạng thái của đồng hồ đếm ngược
    val isCountingDown = remember { mutableStateOf(false) }

    // Khởi tạo một State để lưu trữ giá trị của đồng hồ đếm ngược
    val countDownTime = remember { mutableStateOf(2.minutes) }



    Dialog(
        onDismissRequest = onDismiss
    ) {
        if (showVerifyDialog.value) {
            CustomDialog(
                title = "Xác thực xóa tài khoản",
                onDismiss = { showVerifyDialog.value = false }
            )
        }

        // Xử lý sự kiện khi người dùng nhấn nút Gửi lại
        val onResendClick = {
            // Đặt giá trị của State để dừng đồng hồ đếm ngược và đặt lại giá trị của nó để bắt đầu một chu kỳ mới
//        errorAuthenCode = false
            isCountingDown.value = true
            countDownTime.value = 2.minutes
//        isCountingDown.value = true
            verificationCode = ""

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
                        .clickable(onClick = onDismiss)
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (title != null) {
                    Text(
                        text = title,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (title == "Cập nhật mật khẩu") {
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

                        if (wrongPW) {
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
                                onClick = onDismiss
                            ) {
                                Text(text = "Hủy")
                            }

                            Button(
                                onClick = {

                                }
                            ) {
                                Text(text = "Xong")
                            }
                        }
                    }
                } else if (title == "Xác nhận xóa tài khoản") {
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
                                onClick = onDismiss
                            ) {
                                Text(text = "Hủy")
                            }

                            Button(
                                onClick = {
                                    showVerifyDialog.value = true
                                }
                            ) {
                                Text(text = "Xong")
                            }
                        }
                    }
                } else if (title == "Xác thực xóa tài khoản") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nhập mã xác thực vừa được gửi tới email của bạn",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Mã xác thực",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .width(295.dp)
                                .padding(bottom = 5.dp)
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
                                modifier = Modifier.padding(bottom = 10.dp),
                                color = MaterialTheme.colors.error
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Button(
                                onClick = {

                                }
                            ) {
                                Text(text = "Xong")
                            }

                            TextButton(
                                onClick = onDismiss
                            ) {
                                Text(text = "Hủy")
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Text(text = "Chưa nhận được email? ")

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                text = " Gửi lại",
                                color = MaterialTheme.colors.error,
                                fontWeight = FontWeight.SemiBold,
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
                                            countDownTime.value.toDouble(DurationUnit.MINUTES)
                                                .toInt(),
                                            countDownTime.value.toDouble(DurationUnit.SECONDS)
                                                .toInt() % 60
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
                    }
                }
            }
        }
    }
}

