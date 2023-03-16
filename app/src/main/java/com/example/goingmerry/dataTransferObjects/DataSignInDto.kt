package com.example.goingmerry.dataTransferObjects

import com.google.gson.annotations.SerializedName

data class DataSignInDto (
    @SerializedName("data")
    val dataSignInDto: String
)