package com.example.goingmerry.viewModel

import CreateConversationMutation
import ReplyRequestAddFriendMutation
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import type.ConversationInput
import type.FriendRequestReply

class ListRAFViewModel: ViewModel(){
    private val _res = MutableStateFlow("")
    val res = _res.asStateFlow()
    fun replyRequestAddFriend(token: String, id: String, reply: FriendRequestReply){
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
                    .serverUrl("http://10.0.2.2:8080/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.mutate(ReplyRequestAddFriendMutation(id, reply))
                users.enqueue(object: ApolloCall.Callback<ReplyRequestAddFriendMutation.Data>(){
                    override fun onResponse(response: Response<ReplyRequestAddFriendMutation.Data>) {
                        Log.e("reply", response.data.toString())
                        _res.tryEmit(response.data!!.replyRequest!!.sender!!.id)
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

    fun createConversation(token: String, input: ConversationInput){
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
                    .serverUrl("http://10.0.2.2:8080/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.mutate(CreateConversationMutation(input))
                Log.e("input", input.toString())
                users.enqueue(object: ApolloCall.Callback<CreateConversationMutation.Data>(){
                    override fun onResponse(response: Response<CreateConversationMutation.Data>) {
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

    fun resetRes(){
        _res.tryEmit("")
    }
}