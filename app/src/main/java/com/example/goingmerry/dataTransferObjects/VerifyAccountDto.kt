package com.example.goingmerry.dataTransferObjects

import com.google.gson.annotations.SerializedName

data class VerifyAccountDto(
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String
)

data class ExchangeToken(
    @SerializedName("token") val token: String
)

data class ReTokenDto(
    @SerializedName("email") val email: String,
    @SerializedName("permission") val permission: String
)