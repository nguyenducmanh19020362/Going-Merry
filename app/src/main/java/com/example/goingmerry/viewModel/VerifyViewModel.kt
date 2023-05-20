package com.example.goingmerry.viewModel

import ForgotPasswordDto
import ResetPasswordDto
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.dataTransferObjects.*
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerifyViewModel : ViewModel() {
    var isVerified = mutableStateOf(value = 0)
    var isChanged = mutableStateOf(value = 0)
    var isReqDel = mutableStateOf(value = 0)
    var isDeleted = mutableStateOf(value = 0)
    var isReset = mutableStateOf(value = 0)
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
            val responseService = authService.resetPassword(ResetPasswordDto(password = newPassword, token = resetPasswordToken!!))
            if (responseService.isSuccessful) {
                responseService.body()?.let {
                    if (it.status == "done") {
                        isReset.value = 1
                        Log.e("tag", "Password changed successfully")
                    } else {
                        isReset.value = 2
                        Log.e("tag", "Failed")
                    }
                }
            }
        }
    }

    fun changePassword(token: String, oldPassword: String, newPassword: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService =
                authService.changePassword("Bearer $token", ChangePassword(oldPassword, newPassword))
            if (responseService.isSuccessful) {
                responseService.body()?.let {
                    if (it.status == "done") {
                        isChanged.value = 1
                        Log.e("tag", "Change password successfully!")
                    } else {
                        isChanged.value = 2
                        Log.e("tag", "Change password failed!")
                    }
                }
            }
        }
    }

    fun reqDelAcc(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService =
                authService.reqDelAcc(ReqDelAcc(password))
            if (responseService.isSuccessful) {
                responseService.body()?.let {
                    if (it.status == "done") {
                        isReqDel.value = 1
                        Log.e("tag", "Verify code sent successfully")
                    } else {
                        isReqDel.value = 2
                        Log.e("tag", "Failed")
                    }
                }
            }
        }
    }

    fun deleteAccount(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService =
                authService.deleteAccount(DeleteAccount(token))
            if (responseService.isSuccessful) {
                responseService.body()?.let {
                    if (it.status == "done") {
                        isDeleted.value = 1
                        Log.e("tag", "Deleted account")
                    } else {
                        isDeleted.value = 2
                        Log.e("tag", "Failed")
                    }
                }
            }
        }
    }
}