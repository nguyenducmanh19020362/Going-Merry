package com.example.goingmerry.viewModel

import UpdateAccountMutation
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.goingmerry.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import type.AccountInput
import type.ConversationInput

class FillInfoViewModel: ViewModel() {
    var idAccountUpdate = mutableStateOf("")
    fun updateAccount(token: String, input: AccountInput){
        viewModelScope.launch(Dispatchers.IO){
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
                    .serverUrl("${URL.urlServer}/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.mutate(UpdateAccountMutation(input))
                Log.e("input", input.toString())
                users.enqueue(object: ApolloCall.Callback<UpdateAccountMutation.Data>(){
                    override fun onResponse(response: Response<UpdateAccountMutation.Data>) {
                        idAccountUpdate.value = response.data!!.updateAccount!!.id
                        Log.e("data", response.data.toString())
                    }

                    override fun onFailure(e: ApolloException) {
                        Log.e("Todo", e.toString())
                    }
                })
            }catch(e: Exception){
                Log.d("error", e.toString())
            }
        }
    }
}