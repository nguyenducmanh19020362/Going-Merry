package com.example.goingmerry.ui.signInSignUp

import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.LoginViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.DataStore


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenSignIn(navController: NavController, loginViewModel: LoginViewModel, data: DataStore) {
    var invalidEmailNotification by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var buttonOnClick by rememberSaveable {
        mutableStateOf(false)
    }
    if (buttonOnClick) {
        loginViewModel.login(email, password, data)
        buttonOnClick = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Chào mừng trở lại!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thông tin tài khoản",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .offset(x = (-75).dp)
            )


            InputTextField(email, onValueChange = { email = it })


            InputPasswordField(password, onValueChange = { password = it })

            if (invalidEmailNotification) {
                Text(
                    text = "Email không hợp lệ",
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = MaterialTheme.colors.error
                )
            }

            if (loginViewModel.isSuccessLogin.value == 1) {
                Text(
                    text = "Đăng nhập thất bại, xin hãy thử lại",
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = MaterialTheme.colors.error
                )
            }

            Text(
                text = "Quên mật khẩu?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .offset(x = (-85).dp)
                    .padding(bottom = 10.dp)
                    .clickable(onClick = {
                        navController.navigate(Routes.ForgotPassword.route) {
                            launchSingleTop = true
                        }
                    })
            )

            Button(
                onClick = {
                    invalidEmailNotification = !isValidEmail(email)
                    buttonOnClick = true
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
}

@Preview
@Composable
fun PreviewScreenSignIn() {
    val navController: NavController = rememberNavController()
    val loginViewModel: LoginViewModel = LoginViewModel()
    //ScreenSignIn(navController, loginViewModel)
}


@Composable

fun InputPasswordField(password: String, onValueChange: (String) -> Unit) {

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(60.dp)
            .width(295.dp),
        value = password,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(id = R.string.label_password),
                color = MaterialTheme.colors.onSecondary
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant
        ),
        shape = RoundedCornerShape(10.dp),
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

/*@Preview
@Composable
fun ReviewInputPasswordField() {
    InputPasswordField()
}*/

@Composable
fun InputTextField(text: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(60.dp)
            .width(295.dp),
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(id = R.string.label_email),
                color = MaterialTheme.colors.onSecondary
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant
        ),
        shape = RoundedCornerShape(10.dp),
        maxLines = 1
    )
}

/*
@Composable
@Preview
fun ReviewInputTextField() {
    var text by rememberSaveable { mutableStateOf("") }
    InputTextField(text, onValueChange = { text = it })
}
*/


@Composable
fun LogoApp() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = "",
            modifier = Modifier
                .size(53.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.size(5.dp))

        Text(
            text = "Going Merry",
            fontSize = 45.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/*@Preview
@Composable
fun PreviewLogoApp() {
    LogoApp()
}*/

//Kiểm tra Email có hợp lệ
fun isValidEmail(email: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(email)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
