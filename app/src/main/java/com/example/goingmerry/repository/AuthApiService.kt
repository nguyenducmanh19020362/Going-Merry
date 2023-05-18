package com.example.goingmerry.repository

import ForgotPasswordDto
import ResetPassword
import com.example.goingmerry.dataTransferObjects.*
import com.example.goingmerry.navigate.Routes
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

    @POST("/verify-account")
    suspend fun verifyAccount(@Body body: VerifyAccountDto): Response<ResponseStatus>

    @POST("/exchange-token")
    suspend fun exchangeToken(@Body body: ExchangeToken): Response<ResponseResetPasswordToken>

    @POST("/re-token")
    suspend fun reToken(@Body body: ReTokenDto): Response<Unit>

    @POST("/forgot-password")
    suspend fun forgotPass(@Body body: ForgotPasswordDto): Response<Unit>

    @POST("/reset-password")
    suspend fun resetPassword(@Body body: ResetPassword): Response<Unit>

    @POST("/change-password")
    suspend fun changePassword(@Body body: ChangePassword): Response<Unit>

    @POST("/request-deleted-account")
    suspend fun reqDelAcc(@Body body: ReqDelAcc): Response<Unit>

    @POST("/delete-account")
    suspend fun deleteAccount(@Body body: DeleteAccount): Response<Unit>
}