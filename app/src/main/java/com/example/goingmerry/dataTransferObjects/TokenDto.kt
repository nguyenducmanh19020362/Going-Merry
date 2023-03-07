package com.example.goingmerry.dataTransferObjects

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("data")
    val tokenVerify: String
)