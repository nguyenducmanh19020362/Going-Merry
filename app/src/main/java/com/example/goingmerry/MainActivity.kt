package com.example.goingmerry

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "userinformation"
        ).build()
        val userInformationDao = db.userInformationDao()
        val userInfo: DataUserInfo = DataUserInfo(userInformationDao)
        setContent {
            NewGoingMerryTheme(true) {
                val navController = rememberNavController()
                ScreenStart(loginViewModel, signUpViewModel,homeViewModel, chatBoxViewModel, userInfo, profileViewModel, listRAFViewModel)
//              VerificationScreen(navController = navController, titlee = "Nhập mã đi")
            }
        }
    }
}