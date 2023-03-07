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

object LoginViewModel : ViewModel() {

    private val isSuccessLoading = mutableStateOf(value = false)
    private val loginRequestLiveData = MutableLiveData<Boolean>()

    fun login(email: String, password: String, authLogin: MutableState<Boolean>) {
        Log.e("login", "error0")
        runBlocking {
            try {
                val authService = Retrofit.getAuthService()
                val responseService = authService.getLogin(LoginDto(email = email, password = password))
                Log.e("login", "error1")
                if (responseService.isSuccessful) {
                    Log.e("login", "error2")
                    delay(1500L)
                    isSuccessLoading.value = true
                    authLogin.value = true
                    responseService.body()?.let { tokenDto ->
                        Log.e("Logging", "Response TokenDto: $tokenDto")
                    }
                } else {
                    Log.e("login", "error3")
                    authLogin.value = false
                    responseService.errorBody()?.let { error ->
                        delay(1500L)
                        error.close()
                        Log.e("Logging", "error")
                    }
                }

                loginRequestLiveData.postValue(responseService.isSuccessful)
            } catch (e: Exception) {
                Log.e("Logging", "Error Authentication", e)
            }
        }
    }
}