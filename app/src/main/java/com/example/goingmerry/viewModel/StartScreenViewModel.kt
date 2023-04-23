package com.example.goingmerry.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class StartScreenViewModel: ViewModel() {
    private val _transferHome = MutableStateFlow(false)
    val transferHome = _transferHome.asStateFlow()
    @RequiresApi(Build.VERSION_CODES.O)
    fun setLogin(data: DataStore, loginViewModel: LoginViewModel){
        viewModelScope.launch(Dispatchers.IO){
            val currentTime = Instant.now().epochSecond
            val expiredToken = data.readExpired()
            val token = data.readToken()
            if(expiredToken > currentTime){
                loginViewModel.token.value = token
                loginViewModel.expiredToken.value = Instant.ofEpochSecond(expiredToken).toString()
                loginViewModel.isSuccessLogin.value = 2
            }else{
                loginViewModel.isSuccessLogin.value = 4
            }
        }
    }
}