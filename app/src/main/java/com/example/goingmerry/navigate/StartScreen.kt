package com.example.goingmerry.navigate

import AccountQuery
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.ui.ChatBox
import com.example.goingmerry.ui.home.BodyScreen
import com.example.goingmerry.ui.home.ScreenHome
import com.example.goingmerry.ui.signInSignUp.ScreenSignIn
import com.example.goingmerry.ui.signInSignUp.ScreenSignUp
import com.example.goingmerry.ui.signInSignUp.WelcomeScreen
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.HomeViewModel
import com.example.goingmerry.ui.home.SettingScreen
import com.example.goingmerry.ui.setting.ProfileScreen
import com.example.goingmerry.ui.setting.UserInfoScreen
import com.example.goingmerry.ui.signInSignUp.*
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenStart(loginViewModel: LoginViewModel, signUpViewModel: SignUpViewModel, homeViewModel: HomeViewModel, chatBoxViewModel: ChatBoxViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Welcome.route){
        composable(Routes.ChatBox.route + "/{idConversation}"){navBackTrackEntry->
            val idMember = navBackTrackEntry.arguments?.getString("idConversation")
            idMember?.let {
                Log.e("it", "${it.toInt()}")
                ChatBox(homeViewModel.conversations.value[it.toInt()], chatBoxViewModel)
            }

        }

        composable(Routes.Setting.route){
            SettingScreen(navController = navController)
        }

        composable(Routes.UserInfo.route){
            UserInfoScreen()
        }

        composable(Routes.Profile.route){
            ProfileScreen()
        }

        composable(Routes.FillInfo.route){
            FillScreen(navController = navController)
        }

        composable(Routes.ForgotPassword.route){
            ForgotPasswordScreen(navController = navController)
        }

        composable(Routes.Verification.route){
            VerificationScreen(navController = navController, titlee = "")
        }

        composable(Routes.Welcome.route){
            WelcomeScreen(navController = navController,
                signupViewModel = signUpViewModel, loginViewModel = loginViewModel)
        }

        composable(Routes.SignIn.route){
            if(loginViewModel.isSuccessLogin.value == 2){
                LaunchedEffect(key1 = Unit){
                    navController.navigate(route = Routes.Home.route){
                        popUpTo(route = Routes.SignIn.route) {
                            inclusive = true
                        }
                    }
                }
            }else {
                ScreenSignIn(navController = navController, loginViewModel = loginViewModel)
            }
        }

        composable(Routes.Home.route){
            ScreenHome(loginViewModel, chatBoxViewModel = chatBoxViewModel, homeViewModel = homeViewModel, navController)
        }

        composable(Routes.SignUp.route){
            if(signUpViewModel.isSuccessSignUp.value == 2) {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(route = Routes.Welcome.route) {
                        popUpTo(route = Routes.SignIn.route) {
                            inclusive = true;
                        }
                    }
                }
            }else{
                ScreenSignUp(navController = navController, signUpViewModel = signUpViewModel)
            }
        }
    }
}