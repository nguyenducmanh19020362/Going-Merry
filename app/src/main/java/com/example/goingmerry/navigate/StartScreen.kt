package com.example.goingmerry.navigate

import AccountQuery
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.DataStore
import com.example.goingmerry.ui.ChatBox
import com.example.goingmerry.ui.ChatBoxGroup
import com.example.goingmerry.ui.home.BodyScreen
import com.example.goingmerry.ui.home.ScreenHome
import com.example.goingmerry.ui.signInSignUp.ScreenSignIn
import com.example.goingmerry.ui.signInSignUp.ScreenSignUp
import com.example.goingmerry.ui.signInSignUp.WelcomeScreen
import com.example.goingmerry.ui.home.SettingScreen
import com.example.goingmerry.ui.setting.ListRequestAddFriends
import com.example.goingmerry.ui.setting.ProfileScreen
import com.example.goingmerry.ui.setting.UserInfoScreen
import com.example.goingmerry.ui.signInSignUp.*
import com.example.goingmerry.viewModel.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenStart(loginViewModel: LoginViewModel, signUpViewModel: SignUpViewModel, homeViewModel: HomeViewModel,
                chatBoxViewModel: ChatBoxViewModel, profileViewModel: ProfileViewModel,
                listRAFViewModel: ListRAFViewModel, groupManagerViewModel: GroupManagerViewModel,
                fillInfoViewModel: FillInfoViewModel, startScreenViewModel: StartScreenViewModel, data: DataStore
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Welcome.route){
        composable(Routes.ChatBox.route + "/{idConversation}"){navBackTrackEntry->
            val idMember = navBackTrackEntry.arguments?.getString("idConversation")
            idMember?.let {
                Log.e("it", "${it.toInt()}")
                ChatBox(homeViewModel.conversations.value[it.toInt()], chatBoxViewModel, homeViewModel.idAccount.value)
            }
        }

        composable(Routes.ChatBoxGroup.route + "/{idConversation}"){navBackTrackEntry->
            val idMember = navBackTrackEntry.arguments?.getString("idConversation")
            idMember?.let {
                Log.e("it", "${it.toInt()}")
                ChatBoxGroup(homeViewModel.conversations.value[it.toInt()], chatBoxViewModel, homeViewModel.idAccount.value)
            }

        }

        composable(Routes.Setting.route){
            SettingScreen(navController = navController, homeViewModel.nameAccount.value, homeViewModel.avatarAccount.value,
            homeViewModel.idAccount.value)
        }

        composable(Routes.UserInfo.route){
            UserInfoScreen()
        }

        composable(Routes.Profile.route + "/{idUser}"){navBackTrackEntry->
            val idUser = navBackTrackEntry.arguments?.getString("idUser")
            idUser?.let {
                var isFriend: Boolean = false;
                if(homeViewModel.conversations.value.isNotEmpty()){
                    for(conversation in homeViewModel.conversations.value){
                        if(conversation.members.size == 2){
                            if(conversation.members[0].id == idUser){
                                isFriend = true;
                                break;
                            }
                            if(conversation.members[1].id == idUser){
                                isFriend = true;
                                break;
                            }
                        }
                    }
                }
                ProfileScreen(idUser.orEmpty(), loginViewModel.token.value, profileViewModel, isFriend)
            }
        }

        composable(Routes.FillInfo.route){
            FillScreen(navController = navController, fillInfoViewModel, loginViewModel.token.value)
        }

        composable(Routes.ForgotPassword.route){
            ForgotPasswordScreen(navController = navController)
        }

        composable(Routes.Verification.route){
            VerificationScreen(navController = navController, titlee = "")
        }

        composable(Routes.Welcome.route){
            WelcomeScreen(navController = navController,
                signupViewModel = signUpViewModel, loginViewModel = loginViewModel,
                startScreenViewModel = startScreenViewModel, dataStore = data)
        }

        composable(Routes.SignIn.route){
            if(loginViewModel.isSuccessLogin.value == 2){
                LaunchedEffect(key1 = Unit){
                    chatBoxViewModel.stateSockets.value = "OFF"
                    navController.navigate(route = Routes.Home.route){
                        popUpTo(route = Routes.SignIn.route) {
                            inclusive = true
                        }
                    }
                    loginViewModel.isSuccessLogin.value = 0
                }
            }else if(loginViewModel.isSuccessLogin.value == 3){
                LaunchedEffect(key1 = Unit){
                    navController.navigate(route = Routes.FillInfo.route){
                        popUpTo(route = Routes.SignIn.route){
                            inclusive = true
                        }
                    }
                    loginViewModel.isSuccessLogin.value = 0
                }
            }else{
                ScreenSignIn(navController = navController, loginViewModel = loginViewModel, data = data)
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
        composable(Routes.ListRequestAddFriend.route){
            val listRequestAddFriend = homeViewModel.listRequestAddFriend.collectAsState()
            ListRequestAddFriends(token = loginViewModel.token.value, listFriendRequest = listRequestAddFriend.value, listRAFViewModel = listRAFViewModel)
        }

        composable(Routes.GroupManager.route){
            GroupManager(groupManagerViewModel, loginViewModel.token.value)
        }
    }
}