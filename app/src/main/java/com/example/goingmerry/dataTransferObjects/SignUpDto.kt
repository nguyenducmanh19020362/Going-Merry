package com.example.goingmerry.dataTransferObjects

import com.google.gson.annotations.SerializedName

class SignUpDto (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)