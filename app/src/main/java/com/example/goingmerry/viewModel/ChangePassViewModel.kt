import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePassViewModel: ViewModel() {
    // Email tài khoản quên mật khâir
    fun emailForgotPass(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.forgotPass(ForgotPasswordDto(email))
            if (responseService.isSuccessful) {
                Log.e("tag", "Successfully")
            } else {
                Log.e("tag", "Failed")
            }
        }
    }

    // Đăt lại mật khẩu
    fun resetPassword() {

    }
}