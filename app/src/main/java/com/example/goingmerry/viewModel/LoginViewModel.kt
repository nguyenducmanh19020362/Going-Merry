package com.example.goingmerry.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.dataTransferObjects.LoginDto
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel : ViewModel() {

    val isSuccessLogin = mutableStateOf(value = 0)
    val token = mutableStateOf(value = "")
    val firstLogin = mutableStateOf(false)
    val expiredToken = mutableStateOf("")

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authService = Retrofit.getAuthService()
                val responseService = authService.getLogin(LoginDto(email = email, password = password))

                if (responseService.isSuccessful) {
                    //delay(1500L)
                    isSuccessLogin.value = 2
                    responseService.body()?.let { tokenDto ->
                        token.value = tokenDto.tokenVerify.token;
                        expiredToken.value = tokenDto.tokenVerify.expire
                        firstLogin.value = tokenDto.tokenVerify.firstLogin
                        Log.e("Logging", "Response TokenDto: $tokenDto")
                    }
                } else {
                    isSuccessLogin.value = 1
                    responseService.errorBody()?.let { error ->
                        //delay(1500L)
                        error.close()
                        Log.e("Logging", "error")
                    }
                }

            } catch (e: Exception) {
                Log.e("Logging", "Error Authentication", e)
            }
        }
    }
}