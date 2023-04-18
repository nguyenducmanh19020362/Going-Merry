package com.example.goingmerry

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import java.lang.reflect.Type

object ScreenSizes{
    @Composable
    fun height(): Int{
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp
    }
    @Composable
    fun weight(): Int{
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp
    }
    @Composable
    fun type(): TypeScreen{
        if(height().dp < 700.dp && weight().dp < 480.dp){
            return TypeScreen.Compat
        }else if (height().dp < 900.dp && weight().dp < 840.dp) {
            return TypeScreen.Medium
        }else{
            return TypeScreen.Expanded
        }
    }
}

enum class TypeScreen {
    Compat,
    Medium,
    Expanded
}