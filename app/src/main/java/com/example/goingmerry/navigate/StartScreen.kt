package com.example.goingmerry.navigate

import ChangePasswordScreen
import com.example.goingmerry.viewModel.VerifyViewModel
import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState


import androidx.navigation.compose.rememberNavController
import com.example.goingmerry.DataStore
import com.example.goingmerry.ui.AnonymousChat
import com.example.goingmerry.ui.ChatBox
import com.example.goingmerry.ui.ChatBoxGroup
import com.example.goingmerry.ui.home.ScreenHome
import com.example.goingmerry.ui.signInSignUp.ScreenSignIn
import com.example.goingmerry.ui.signInSignUp.ScreenSignUp
import com.example.goingmerry.ui.signInSignUp.WelcomeScreen
import com.example.goingmerry.ui.home.SettingScreen
import com.example.goingmerry.ui.setting.*
import com.example.goingmerry.ui.signInSignUp.*
import com.example.goingmerry.viewModel.*


@Composable
fun ScreenStart(
    loginViewModel: LoginViewModel, signUpViewModel: SignUpViewModel,
    verifyViewModel: VerifyViewModel, homeViewModel: HomeViewModel,
    chatBoxViewModel: ChatBoxViewModel, profileViewModel: ProfileViewModel,
    listRAFViewModel: ListRAFViewModel, groupManagerViewModel: GroupManagerViewModel,
    fillInfoViewModel: FillInfoViewModel, startScreenViewModel: StartScreenViewModel,
    anonymousChatViewModel: AnonymousChatViewModel, data: DataStore
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Welcome.route){
        composable(Routes.ChatBox.route + "/{idConversation}"){navBackTrackEntry->
            val idMember = navBackTrackEntry.arguments?.getString("idConversation")
            idMember?.let {
                ChatBox(homeViewModel.conversations.value[it.toInt()], chatBoxViewModel, navController, homeViewModel.idAccount.value,
                loginViewModel.token.value)
            }
        }

        composable(Routes.GroupMember.route + "/{idConversation}"){navBackTrackEntry->
            val idConversation = navBackTrackEntry.arguments?.getString("idConversation")
            idConversation?.let {
                for(conversation in homeViewModel.conversations.value){
                    if(idConversation == conversation.id){
                        ListMembers(members = conversation.members, loginViewModel.token.value)
                        break;
                    }
                }
            }
        }

        composable(Routes.ChatBoxGroup.route + "/{idConversation}"){navBackTrackEntry->
            val idMember = navBackTrackEntry.arguments?.getString("idConversation")
            idMember?.let {
                Log.e("it", "${it.toInt()}")
                ChatBoxGroup(homeViewModel.conversations.value[it.toInt()], chatBoxViewModel, homeViewModel.idAccount.value,
                navController, loginViewModel.token.value)
            }

        }

        composable(Routes.Setting.route){
            SettingScreen(navController, homeViewModel.nameAccount.value, homeViewModel.avatarAccount.value,
            homeViewModel.idAccount.value, data, chatBoxViewModel, anonymousChatViewModel, loginViewModel.token.value)
        }

        composable(Routes.UserInfo.route){
            UserInfoScreen()
        }

        composable(Routes.Profile.route + "/{idUser}"){navBackTrackEntry->
            val idUser = navBackTrackEntry.arguments?.getString("idUser")
            idUser?.let {
                var isFriend: String = "Sửa thông tin";
                if(idUser != homeViewModel.idAccount.value) {
                    if(homeViewModel.conversations.value.isNotEmpty()){
                        for(conversation in homeViewModel.conversations.value){
                            if(conversation.members.size == 2){
                                if(conversation.members[0].id == idUser){
                                    isFriend = "Xóa bạn";
                                    break;
                                }
                                if(conversation.members[1].id == idUser){
                                    isFriend = "Xóa bạn";
                                    break;
                                }
                            }
                        }
                    }
                    if(isFriend != "Xóa bạn"){
                        isFriend = "Thêm bạn";
                    }
                }
                ProfileScreen(idUser, loginViewModel.token.value, profileViewModel, isFriend, navController)
            }
        }

        composable(Routes.FillInfo.route){
            FillScreen(navController = navController, fillInfoViewModel, profileViewModel, loginViewModel.token.value)
        }

        composable(Routes.ForgotPassword.route){
            ForgotPasswordScreen(navController = navController, verifyViewModel = VerifyViewModel())
        }

        composable(Routes.Verification.route + "/{email}/{typeToken}"){
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val email = navBackStackEntry?.arguments?.getString("email")
            val typeToken = navBackStackEntry?.arguments?.getString("typeToken")
            VerificationScreen(navController = navController, verifyViewModel = verifyViewModel, email = email, typeToken = typeToken)
        }

        composable(Routes.ChangePassword.route + "/{verificationCode}") {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val verifyCode = navBackStackEntry?.arguments?.getString("verificationCode")
            ChangePasswordScreen(navController = navController,
                verifyViewModel = verifyViewModel, tokenResetPassword = verifyCode)
        }

        composable(Routes.Welcome.route){
            profileViewModel.reset()
            WelcomeScreen(navController = navController,
                signupViewModel = signUpViewModel, loginViewModel = loginViewModel,
                startScreenViewModel = startScreenViewModel, dataStore = data)
        }

        composable(Routes.SignIn.route){
            chatBoxViewModel.stateSockets.value = "OFF"
            Log.e("StartScreen", loginViewModel.isSuccessLogin.value.toString())
            if(loginViewModel.isSuccessLogin.value == 2){
                Log.e("StartScreen", loginViewModel.firstLogin.value.toString())
                LaunchedEffect(key1 = Unit){
                    navController.navigate(route = Routes.Home.route){
                        popUpTo(route = Routes.SignIn.route) {
                            inclusive = true
                        }
                    }
                }
            }else if(loginViewModel.isSuccessLogin.value == 3){
                LaunchedEffect(key1 = Unit){
                    navController.navigate(route = Routes.FillInfo.route){
                        popUpTo(route = Routes.SignIn.route){
                            inclusive = true
                        }
                    }
                }
            }else{
                ScreenSignIn(navController = navController, loginViewModel = loginViewModel, data = data)
            }
        }

        composable(Routes.Home.route){
            chatBoxViewModel.resetListReceiverMessage()
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
            ListRequestAddFriends(token = loginViewModel.token.value, listFriendRequest = listRequestAddFriend.value, listRAFViewModel = listRAFViewModel,
                        navController)
        }

        composable(Routes.GroupManager.route){
            GroupManager(groupManagerViewModel, loginViewModel.token.value, navController)
        }

        composable(Routes.GroupMemberManager.route + "/{idGroup}"){
                navBackTrackEntry->
            val idGroup = navBackTrackEntry.arguments?.getString("idGroup")
            idGroup.let {
                for(group in groupManagerViewModel.listGroups.value){
                    if(group.id == idGroup){
                        groupManagerViewModel.state.value = false
                        MemberGroupManager(
                            listMembers = group.members,
                            groupManagerViewModel = groupManagerViewModel,
                            idGroup = idGroup,
                            nameGroup = group.name,
                            token = loginViewModel.token.value
                        )
                        break;
                    }
                }
            }
        }
        composable(Routes.AnonymousChat.route){
            AnonymousChat(loginViewModel, anonymousChatViewModel, navController, homeViewModel.stateIncognito.value)
        }
    }
}