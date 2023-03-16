package com.example.goingmerry.ui.signInSignUp

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
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel

@Composable
fun ScreenSignUp(navController: NavController, signUpViewModel: SignUpViewModel) {
    var invalidPasswordNotification by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("")}
    var rePassword by rememberSaveable { mutableStateOf("") }

    var isValid = rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(25.dp))

        /*Text(
            text = "Tạo tài khoản của riêng bạn!",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 50.dp)
        )*/

        Column {
            Text(
                text = "Thông tin tài khoản",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            InputUserNameField(text, onValueChange = { text = it })

            InputTextField(text, onValueChange = { text = it })

            InputPasswordField(password, onValueChange = {password = it})

            InputPasswordField(rePassword, onValueChange = {rePassword = it})

            if (invalidPasswordNotification) {
                Text(
                    text = "Password nhập lại không đúng",
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = MaterialTheme.colors.error
                )
            }

            Text(
                text = "Xem chính sách quyền riêng tư của Going Merry",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Button(
                onClick = {
                    if(password != rePassword){
                        invalidPasswordNotification = true;
                    }else{
                        invalidPasswordNotification = false;
                        signUpViewModel.signUp(email = text, password = password)
                        navController.navigate(Routes.Welcome.route)
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
        }
    }
}

/*@Preview
@Composable
fun PreviewScreenSignUp() {
    ScreenSignUp()
}*/

@Composable
fun InputUserNameField(text: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(60.dp)
            .width(295.dp),
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(id = R.string.label_userName),
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

@Composable
@Preview
fun ReviewInputUserNameField() {
    var text by rememberSaveable { mutableStateOf("") }
    InputUserNameField(text, onValueChange = { text = it })
}


@Composable
fun InputRePasswordField() {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(60.dp)
            .width(295.dp),
        value = password,
        onValueChange = { password = it },
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

@Preview
@Composable
fun ReviewInputRePasswordField() {
    InputRePasswordField()
}