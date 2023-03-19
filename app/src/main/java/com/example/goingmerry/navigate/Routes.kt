package com.example.goingmerry.navigate

sealed class Routes (val route: String) {
    object SignIn: Routes("signIn")
    object Home: Routes("home")
    object Welcome: Routes("welcome")
    object SignUp: Routes("signUp")
    object ChatBox: Routes("chatBox")
}