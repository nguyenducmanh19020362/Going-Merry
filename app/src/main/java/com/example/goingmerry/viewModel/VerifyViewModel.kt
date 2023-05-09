package com.example.goingmerry.viewModel

import ForgotPasswordDto
import ResetPassword
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.dataTransferObjects.ExchangeToken
import com.example.goingmerry.dataTransferObjects.ReTokenDto
import com.example.goingmerry.dataTransferObjects.VerifyAccountDto
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerifyViewModel : ViewModel() {
    var isVerified = mutableStateOf(value = 0)
    var resetPasswordToken: String? = null

    // Xác thực đăng ký tài khoản
    fun verifyAccount(email: String, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService =
                authService.verifyAccount(VerifyAccountDto(email = email, token = token))
            if (responseService.isSuccessful) {
                responseService.body()?.let {
                    if (it.status == "done") {
                        isVerified.value = 1
                        Log.e("tag", "Account verified")
                    } else isVerified.value = 2
                }
            }
        }
    }

    // Gửi mã tới email quên mật khẩu
    fun forgotPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.forgotPass(ForgotPasswordDto(email = email))
            if (responseService.isSuccessful) {
                Log.e("tag", "Sent successful")
            } else {
                Log.e("tag", "Failed")
            }
        }
    }

    // Xác thực đổi mật khẩu
    fun exchangeToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.exchangeToken(ExchangeToken(token = token))
            if (responseService.isSuccessful) {
                responseService.body()?.let {
                    resetPasswordToken = it.responseToken
                    Log.e("tag", "verified successfully")
                }
            }
        }
    }

    //     Gửi lại mã xác thực
    fun reToken(email: String, per: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService =
                authService.reToken(ReTokenDto(email = email, permission = per))
            if (responseService.isSuccessful) {
                Log.e("tag", "Account verified")
            } else {
                Log.e("tag", "Account verification failed")
            }
        }
    }

    // Đặt lại mật khẩu mới
    fun resetPassword(newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (resetPasswordToken == null) {
                Log.e("tag", "Reset password token is not set")
                return@launch
            }

            val authService = Retrofit.getAuthService()
            Log.e("tag", resetPasswordToken.toString())
            val responseService = authService.resetPassword(ResetPassword(password = newPassword, token = resetPasswordToken!!))
            if (responseService.isSuccessful) {
                Log.e("tag", "Password changed successfully")
            }
        }
    }
}