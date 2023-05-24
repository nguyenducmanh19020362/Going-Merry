package com.example.goingmerry.ui.signInSignUp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.SignUpViewModel

@Composable
fun ScreenSignUp(navController: NavController, signUpViewModel: SignUpViewModel) {
    var invalidPasswordNotification by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rePassword by rememberSaveable { mutableStateOf("") }
    val typeToken = "verify-account"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tạo tài khoản của riêng bạn!",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 20.dp)
            )

            Text(
                text = "Thông tin tài khoản",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .width(295.dp)
            )


            InputTextField(email, onValueChange = { email = it })

            InputPasswordField(password, onValueChange = { password = it })

            InputRePasswordField(rePassword, onValueChange = { rePassword = it })

            if (invalidPasswordNotification) {
                Text(
                    text = "Password nhập lại không đúng",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(bottom = 10.dp).width(295.dp),
                    color = MaterialTheme.colors.error
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (password != rePassword) {
                        invalidPasswordNotification = true;
                    } else {
                        signUpViewModel.signUp(inputEmail = email, password = password)
                        navController.navigate(
                            route = Routes.Verification.route + "/$email/$typeToken",
                            builder = {
                                launchSingleTop = true
                            }
                        )
                    }
                },
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                modifier = Modifier
                    .height(60.dp)
                    .width(295.dp)

            ) {
                Text(text = "Đăng ký")
            }

            Row(
                modifier = Modifier.padding(top = 10.dp).width(295.dp)
            ) {
                Text(
                    text = "Đã có tài khoản? ",
                    textAlign = TextAlign.Left,
                    color = Color.Black
                )

                Text(
                    text = " Đăng nhập",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .clickable(onClick = {
                            navController.navigate(Routes.SignIn.route) {
                                launchSingleTop = true
                            }
                        })
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreenSignUp() {
    val navController = rememberNavController()
    val signUpViewModel: SignUpViewModel = SignUpViewModel()
    ScreenSignUp(navController, signUpViewModel)
}

@Composable
fun InputRePasswordField(rePassword: String, onValueChange: (String) -> Unit) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(60.dp)
            .width(295.dp),
        value = rePassword,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(id = R.string.label_rePassword),
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

