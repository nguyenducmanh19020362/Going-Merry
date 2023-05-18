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

data class ResponseStatus(
    @SerializedName("status") val status: String
)

data class ResponseResetPasswordToken(
    @SerializedName("data") val responseToken: String
)

data class ChangePassword(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String
)

data class ReqDelAcc(
    @SerializedName("password") val password: String
)

data class DeleteAccount(
    @SerializedName("token") val token: String
)