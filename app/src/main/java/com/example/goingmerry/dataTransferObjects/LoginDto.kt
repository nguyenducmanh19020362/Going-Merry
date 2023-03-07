package com.example.goingmerry.dataTransferObjects

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("username") val email: String,
    @SerializedName("password") val password: String
)