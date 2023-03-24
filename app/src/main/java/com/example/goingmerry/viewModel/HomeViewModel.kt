package com.example.goingmerry.viewModel

import FindUsersQuery
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

object HomeViewModel: ViewModel(){
    fun findPeoples(matcher: String, model: LoginViewModel){
        viewModelScope.launch (Dispatchers.IO){
            try {
                Log.e("log//", model.token.value.toString())
                Log.e("log", matcher)
                val okHttp = OkHttpClient.Builder()
                    .addInterceptor{chain ->
                        val original = chain.request()
                        val builder = original.newBuilder().method("POST", original.body)
                        builder.addHeader("Authorization", "Bearer ${model.token.value}")
                        builder.addHeader("Content-Type","application/json")
                        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                        chain.proceed(builder.build())
                    }.build()
                val apolloClient = ApolloClient.builder()
                    .serverUrl("http://10.0.2.2:8080/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.query(FindUsersQuery(matcher = matcher))
                users.enqueue(object: ApolloCall.Callback<FindUsersQuery.Data>(){
                    override fun onResponse(response: Response<FindUsersQuery.Data>) {
                        Log.e("Abc", response.data.toString())
                    }

                    override fun onFailure(e: ApolloException) {
                        Log.e("Todo", e.toString())
                    }
                })
            }catch (e: Exception){
                Log.d("error", e.toString())
            }
        }
    }
}