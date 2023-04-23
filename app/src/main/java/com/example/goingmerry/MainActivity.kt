package com.example.goingmerry

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.navigate.ScreenStart
import com.example.goingmerry.ui.theme.NewGoingMerryTheme
import com.example.goingmerry.viewModel.*

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val chatBoxViewModel: ChatBoxViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val listRAFViewModel: ListRAFViewModel by viewModels()
    private val groupManagerViewModel: GroupManagerViewModel by viewModels()
    private val fillInfoViewModel: FillInfoViewModel by viewModels()
    private val startScreenViewModel: StartScreenViewModel by viewModels()
    private val Context.dataStore by preferencesDataStore(
        name = "token"
    )
    private val data: DataStore by lazy {
        DataStore(
            applicationContext.getSharedPreferences("token", Context.MODE_PRIVATE),
            dataStore
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewGoingMerryTheme(true) {
                Log.e("size", "${ScreenSizes.weight()} ${ScreenSizes.height()}")
                val navController = rememberNavController()
                ScreenStart(loginViewModel, signUpViewModel,homeViewModel, chatBoxViewModel,
                    profileViewModel, listRAFViewModel, groupManagerViewModel, fillInfoViewModel, startScreenViewModel,  data)
//              VerificationScreen(navController = navController, titlee = "Nhập mã đi")
            }
        }
    }
}