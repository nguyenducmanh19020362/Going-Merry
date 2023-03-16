package com.example.goingmerry.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.ui.home.ScreenHome
import com.example.goingmerry.ui.signInSignUp.ScreenSignIn
import com.example.goingmerry.ui.signInSignUp.ScreenSignUp
import com.example.goingmerry.ui.signInSignUp.WelcomeScreen
import com.example.goingmerry.viewModel.LoginViewModel
import com.example.goingmerry.viewModel.SignUpViewModel


@Composable
fun ScreenStart(loginViewModel: LoginViewModel, signUpViewModel: SignUpViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Welcome.route){
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
            ScreenHome()
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