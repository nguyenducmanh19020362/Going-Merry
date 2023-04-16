package com.example.goingmerry.navigate

sealed class Routes (val route: String) {
    object SignIn: Routes("signIn")
    object Home: Routes("home")
    object Welcome: Routes("welcome")
    object SignUp: Routes("signUp")
    object ChatBox: Routes("chatBox")

    object FillInfo: Routes("fill")

    object Setting: Routes("setting")

    object UserInfo: Routes("userInfo")

    object Profile: Routes("profile")

    object ForgotPassword: Routes("forgotPassword")

    object Verification: Routes("verification")
    object ListRequestAddFriend: Routes("listRequestAddFriend")

    object ChatBoxGroup: Routes("chatBoxGroup")
}