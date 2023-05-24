import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.ui.signInSignUp.InputPasswordField
import com.example.goingmerry.ui.signInSignUp.InputRePasswordField
import com.example.goingmerry.ui.signInSignUp.LogoApp
import com.example.goingmerry.viewModel.VerifyViewModel

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    verifyViewModel: VerifyViewModel,
    tokenResetPassword: String?
) {
    var password by rememberSaveable { mutableStateOf("") }
    var rePassword by rememberSaveable { mutableStateOf("") }
    var invalidPasswordNotification by rememberSaveable { mutableStateOf(false) }
    var errorChanged by rememberSaveable { mutableStateOf(false) }
    var verifyOnclick by rememberSaveable { mutableStateOf(false) }
    val ctx = LocalContext.current

    if (verifyOnclick) {
        if (password != rePassword) {
            invalidPasswordNotification = true
        } else {
            if (tokenResetPassword != null) {
                if (verifyViewModel.resetPasswordToken != null) {
                    verifyViewModel.resetPassword(newPassword = password)
                    when (verifyViewModel.isReset.value) {
                        1 -> {
                            Toast.makeText(ctx, "Mật khẩu đã được đặt lại!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(Routes.SignIn.route) {
                                launchSingleTop = true
                            }
                        }
                        2 -> errorChanged = true

                    }
                } else {
                    errorChanged = true
                }
            }
        }

        verifyOnclick = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Đặt lại mật khẩu",
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(320.dp)
                .padding(bottom = 25.dp)
        )

        InputPasswordField(password, onValueChange = { password = it })

        InputRePasswordField(rePassword, onValueChange = { rePassword = it })

        Spacer(modifier = Modifier.height(10.dp))

        if (errorChanged) {
            Text(
                text = "Đổi mật khẩu thất bại",
                modifier = Modifier.padding(bottom = 10.dp),
                color = MaterialTheme.colors.error
            )
        }

        Row() {
            Button(
                onClick = {
                    verifyOnclick = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(140.dp)

            ) {
                Text(text = "Xác nhận")
            }

            Spacer(modifier = Modifier.width(15.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.Welcome.route) {
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