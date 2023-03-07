package com.example.goingmerry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.goingmerry.ui.signInSignUp.ScreenSignIn
import com.example.goingmerry.ui.theme.NewGoingMerryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewGoingMerryTheme() {
                ScreenSignIn()
            }
        }
    }
}