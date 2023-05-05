package com.example.goingmerry.viewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.dataTransferObjects.ExchangeToken
import com.example.goingmerry.dataTransferObjects.VerifyAccountDto
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerifyViewModel: ViewModel() {
    // Xác thực đăng ký tài khoản
    fun verifyAccount(email: String, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.verifyAccount(VerifyAccountDto(email = email, token = token))
            if (responseService.isSuccessful) {
                Log.e("tag", "Account verified")
            } else {
                Log.e("tag", "Account verification failed")
            }
        }
    }

    // Xác thực đổi mật khẩu
    fun exchangeToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.exchangeToken(ExchangeToken(token = token))
            if (responseService.isSuccessful) {
                Log.e("tag", "Confirm successful password change")
            } else {
                Log.e("tag", "Incorrect code")
            }
        }
    }

//     Gửi lại mã xác thực
//    fun reToken(token: String) {
//        val email = mutableStateOf("")
//        viewModelScope.launch(Dispatchers.IO) {
//            val authService = Retrofit.getAuthService()
//            val responseService = authService.verifyAccount(VerifyAccountDto(email = email.value, token = token))
//            if (responseService.isSuccessful) {
//                Log.e("tag", "Account verified")
//            } else {
//                Log.e("tag", "Account verification failed")
//            }
//        }
//    }
}