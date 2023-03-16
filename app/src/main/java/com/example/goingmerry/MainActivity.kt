package com.example.goingmerry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.goingmerry.navigate.ScreenStart
import com.example.goingmerry.ui.home.ScreenHome

import com.example.goingmerry.ui.signInSignUp.ScreenSignIn
import com.example.goingmerry.ui.theme.NewGoingMerryTheme
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewGoingMerryTheme (true){
                ScreenStart(loginViewModel, signUpViewModel)
            }
        }
    }
}