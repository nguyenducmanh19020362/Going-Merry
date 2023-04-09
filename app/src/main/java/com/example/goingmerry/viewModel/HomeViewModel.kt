package com.example.goingmerry.viewModel

import AccountQuery
import FindUsersQuery
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class HomeViewModel: ViewModel(){
    private val _conversations = MutableStateFlow(listOf<AccountQuery.Conversation>())
    val conversations = _conversations.asStateFlow()
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
    fun account(token: String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val okHttp = OkHttpClient.Builder()
                    .addInterceptor{chain ->
                        val original = chain.request()
                        val builder = original.newBuilder().method("POST", original.body)
                        builder.addHeader("Authorization", "Bearer $token")
                        builder.addHeader("Content-Type","application/json")
                        chain.proceed(builder.build())
                    }.build()
                val apolloClient = ApolloClient.builder()
                    .serverUrl("http://10.0.2.2:8080/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val conversation = apolloClient.query(AccountQuery())
                conversation.enqueue(object: ApolloCall.Callback<AccountQuery.Data>(){
                    override fun onResponse(response: Response<AccountQuery.Data>) {
                        if(response.data != null){
                            //Log.e("dbt", response.data.toString())
                            _conversations.tryEmit(response.data!!.account.conversations)
                        }
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