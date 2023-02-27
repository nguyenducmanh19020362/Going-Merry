package com.example.goingmerry.ui.signInSignUp

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goingmerry.R


@Composable
fun ScreenSignIn(){
    var invalidEmailNotification by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Chào mừng trở lại!",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 50.dp)
        )

        Column {
            Text(
                text = "Thông tin tài khoản",
                modifier = Modifier.padding(bottom = 10.dp)
            )

            InputTextField(text, onValueChange = {text = it})

            InputPasswordField()

            if (invalidEmailNotification){
                Text(
                    text = "Email không hợp lệ",
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = MaterialTheme.colors.error
                )
            }
            
            Text(
                text = "Quên mật khẩu?",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }

        Button(
            onClick = {
                invalidEmailNotification = !isValidEmail(text)
            },
            colors = ButtonDefaults
                .buttonColors(backgroundColor = MaterialTheme.colors.primary),

            ) {
            Text(text = "Đăng nhập")
        }
    }
}

@Preview
@Composable
fun PreviewScreenSignIn(){
    ScreenSignIn()
}


@Composable
fun InputPasswordField(){
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = Modifier.padding(bottom = 10.dp),
        value = password,
        onValueChange = {password = it},
        label = {
            Text(
                text = stringResource(id = R.string.label_password),
                color = MaterialTheme.colors.onSecondary
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
        shape = RoundedCornerShape(15.dp),
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, description)
            }
        }
    )
}

@Preview
@Composable
fun ReviewInputPasswordField(){
    InputPasswordField()
}

@Composable
fun InputTextField(text: String, onValueChange: (String) -> Unit ){
    TextField(
        modifier = Modifier.padding(bottom = 10.dp),
        value = text,
        onValueChange = onValueChange,
        label = { 
            Text(
                text = stringResource(id = R.string.label_email),
                color = MaterialTheme.colors.onSecondary
            ) 
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
        shape = RoundedCornerShape(15.dp),
        maxLines = 1
    )
}

@Composable
@Preview
fun ReviewInputTextField(){
    var text by rememberSaveable { mutableStateOf("") }
    InputTextField(text, onValueChange = {text = it})
}


@Composable
fun LogoApp(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = "Going Merry",
            fontSize = 40.sp
        )
    }
}

@Preview
@Composable
fun PreviewLogoApp(){
    LogoApp()
}

//Kiểm tra Email có hợp lệ
fun isValidEmail(email: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(email)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}