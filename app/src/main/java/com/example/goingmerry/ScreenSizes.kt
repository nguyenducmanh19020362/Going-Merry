package com.example.goingmerry

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

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
}