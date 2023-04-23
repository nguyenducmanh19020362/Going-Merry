package com.example.goingmerry.dataTransferObjects

import java.sql.Time
import java.time.Instant

data class Token (
    val token: String,
    val expire:String,
    val enough: Boolean
)