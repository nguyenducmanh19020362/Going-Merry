package com.example.goingmerry.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.dataTransferObjects.SignUpDto
import com.example.goingmerry.repository.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel(){
    val isSuccessSignUp = mutableStateOf(value = 1)
    fun signUp(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.getSignUp(SignUpDto(email = email, password = password))
            Log.e("body", responseService.body().toString())
            if(responseService.isSuccessful){
                isSuccessSignUp.value = 2
                Log.e("tag","true")
            }else{
                isSuccessSignUp.value = 1
                Log.e("tag","false")
            }
        }
    }
}