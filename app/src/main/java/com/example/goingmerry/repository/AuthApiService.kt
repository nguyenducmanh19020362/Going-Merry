package com.example.goingmerry.repository

import com.example.goingmerry.dataTransferObjects.DataSignInDto
import com.example.goingmerry.dataTransferObjects.LoginDto
import com.example.goingmerry.dataTransferObjects.SignUpDto
import com.example.goingmerry.dataTransferObjects.TokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/login")
    suspend fun getLogin(@Body loginDto: LoginDto) : Response<TokenDto>

    @POST("/signup")
    suspend fun getSignUp(@Body signUpDto: SignUpDto): Response<DataSignInDto>
}