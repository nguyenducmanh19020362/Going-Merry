package com.example.goingmerry.repository

import com.example.goingmerry.dataTransferObjects.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("/login")
    suspend fun getLogin(@Body loginDto: LoginDto) : Response<TokenDto>

    @POST("/signup")
    suspend fun getSignUp(@Body signUpDto: SignUpDto): Response<DataSignInDto>

    @POST("/incognito")
    suspend fun controlChat(@Header("Authorization") token: String): Response<DataControlAnonymousChat>
}