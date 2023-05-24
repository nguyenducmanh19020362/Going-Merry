package com.example.goingmerry.repository

import android.util.Log
import com.example.goingmerry.URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {

    private val retrofit: Retrofit

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

        val builder = Retrofit.Builder()
            .baseUrl("${URL.urlServer}")
            .addConverterFactory(GsonConverterFactory.create(gson))

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES).build()
        retrofit = builder.client(okHttpClient).build()
    }

    fun getAuthService() : AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}