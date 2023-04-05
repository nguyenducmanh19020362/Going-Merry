package com.example.goingmerry.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goingmerry.R

@Composable
fun UserInfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBarUserInfo()

        BodyUserInfo()
    }
}

@Composable
@Preview
fun PreviewUserInfo() {
    UserInfoScreen()
}

@Composable
fun TopBarUserInfo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colors.primary)
            .padding(5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.width(35.dp))

        Text(
            text = "Tài khoản",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.White
        )
    }
}

@Composable
fun BodyUserInfo() {
    val showDialog = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Thông tin tài khoản",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Chỉnh sửa tên tài khoản
        ChangUserName()

        //Email
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "LisaBlackPink@gmail.com",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Thay đổi mật khẩu
        ChangePassword()

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Quản lý tài khoản",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Vô hiệu hóa tài khoản",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    showDialog.value = true

                })
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Xóa tài khoản",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun ChangUserName() {
    val showDialog = remember { mutableStateOf(false) }
    Column() {
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(
                    onClick = { showDialog.value = true })
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Tên tài khoản",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row() {
                    Text(
                        text = "Lisa #0007",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Image(
                        painter = painterResource(id = R.drawable._right_arrow),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChangePassword() {
    val showDialog = remember { mutableStateOf(false) }
//    val sheetState by rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Column() {
        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
//                .clickable(onClick = {showDialog.value = true})
//                .clickable(onClick = {sheetState.show()})
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Thay đổi mật khẩu",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Image(
                    painter = painterResource(id = R.drawable._right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

    }
}

@Composable
fun ChangePasswordDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (newPassword: String) -> Unit
) {
    if (showDialog) {
        var newPassword by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Đổi mật khẩu") },
            text = {
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave(newPassword)
                        onDismiss()
                    }
                ) {
                    Text("Lưu thay đổi")
                }
            },
            dismissButton = {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Đóng dialog")
                }
            }
        )
    }
}

//@Composable
//fun ChangePasswordDialog(onDismiss: () -> Unit) {
//    var oldPassword by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//
//    ModalBottomSheetLayout(
//        sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
//        sheetContent = {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = 8.dp
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Text(
//                        text = "Đổi mật khẩu",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    OutlinedTextField(
//                        value = oldPassword,
//                        onValueChange = { oldPassword = it },
//                        label = { Text("Mật khẩu cũ") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    OutlinedTextField(
//                        value = newPassword,
//                        onValueChange = { newPassword = it },
//                        label = { Text("Mật khẩu mới") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        Button(
//                            onClick = { /* Handle save password */ },
//                            modifier = Modifier.padding(end = 8.dp)
//                        ) {
//                            Text("Lưu thay đổi")
//                        }
//                        IconButton(onClick = { onDismiss() }) {
//                            Icon(Icons.Default.Close, contentDescription = "Close dialog")
//                        }
//                    }
//                }
//            }
//        },
//        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//        content = {
//            Card(
//                modifier = Modifier.fillMaxWidth().clickable { /* Show dialog */ },
//                elevation = 8.dp
//            ) {
//                Text(text = "Đổi mật khẩu")
//            }
//        }
//    )
//}




