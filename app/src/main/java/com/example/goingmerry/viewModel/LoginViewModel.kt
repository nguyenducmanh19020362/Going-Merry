package com.example.goingmerry.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.DataStore
import com.example.goingmerry.dataTransferObjects.LoginDto
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class LoginViewModel : ViewModel() {

    val isSuccessLogin = mutableStateOf(value = 0)
    val token = mutableStateOf(value = "")
    val firstLogin = mutableStateOf(false)
    val expiredToken = mutableStateOf("")

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(email: String, password: String, data: DataStore) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authService = Retrofit.getAuthService()
                val responseService = authService.getLogin(LoginDto(email = email, password = password))

                if (responseService.isSuccessful) {
                    //delay(1500L)
                    responseService.body()?.let { tokenDto ->
                        token.value = tokenDto.tokenVerify.token
                        expiredToken.value = tokenDto.tokenVerify.expire
                        data.saveToken(token.value, Instant.parse(expiredToken.value).epochSecond)
                        firstLogin.value = tokenDto.tokenVerify.enough
                        Log.e("Logging", "Response TokenDto: $tokenDto")
                    }
                    if(!firstLogin.value){
                        isSuccessLogin.value = 3
                    }else{
                        isSuccessLogin.value = 2
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