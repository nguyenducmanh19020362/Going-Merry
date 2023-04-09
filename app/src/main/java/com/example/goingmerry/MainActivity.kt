package com.example.goingmerry

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.navigate.ScreenStart
import com.example.goingmerry.ui.theme.NewGoingMerryTheme
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.HomeViewModel
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val chatBoxViewModel: ChatBoxViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            NewGoingMerryTheme(true) {
                val navController = rememberNavController()
                ScreenStart(loginViewModel, signUpViewModel)
//              VerificationScreen(navController = navController, titlee = "Nhập mã đi")

            }
        }
    }
}