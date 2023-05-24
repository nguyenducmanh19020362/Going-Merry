package com.example.goingmerry.viewModel

import AccountQuery
import FindUsersQuery
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.goingmerry.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class HomeViewModel: ViewModel(){
    private val _listPeople = MutableStateFlow(listOf<FindUsersQuery.FindUser>())
    val listPeople = _listPeople.asStateFlow()

    private val _conversations = MutableStateFlow(listOf<AccountQuery.Conversation>())
    val conversations = _conversations.asStateFlow()

    private val _listRequestAddFriend = MutableStateFlow(listOf<AccountQuery.FriendRequest>())
    val listRequestAddFriend = _listRequestAddFriend.asStateFlow()

    val idAccount = mutableStateOf("")
    val nameAccount = mutableStateOf("")
    val avatarAccount =  mutableStateOf("")
    val stateIncognito = mutableStateOf(false)
    val firstLogin = mutableStateOf(false)
    fun findPeoples(matcher: String, model: LoginViewModel){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val okHttp = OkHttpClient.Builder()
                    .addInterceptor{chain ->
                        val original = chain.request()
                        val builder = original.newBuilder().method("POST", original.body)
                        builder.addHeader("Authorization", "Bearer ${model.token.value}")
                        builder.addHeader("Content-Type","application/json")
                        chain.proceed(builder.build())
                    }.build()
                val apolloClient = ApolloClient.builder()
                    .serverUrl("${URL.urlServer}/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.query(FindUsersQuery(matcher = matcher))
                users.enqueue(object: ApolloCall.Callback<FindUsersQuery.Data>(){
                    override fun onResponse(response: Response<FindUsersQuery.Data>) {
                        _listPeople.tryEmit(response.data!!.findUsers.orEmpty())
                        Log.e("data", response.data.toString())
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
        Log.e("account", "account");
        viewModelScope.launch (Dispatchers.Main){
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
                val conversation = apolloClient.query(AccountQuery())
                conversation.enqueue(object: ApolloCall.Callback<AccountQuery.Data>(){
                    override fun onResponse(response: Response<AccountQuery.Data>) {
                            //Log.e("dbt", response.data.toString())
                        _conversations.tryEmit(response.data!!.account.conversations)
                        idAccount.value = response.data!!.account.id
                        nameAccount.value = response.data!!.account.name
                        if(nameAccount.value == ""){
                            firstLogin.value = true
                        }
                        avatarAccount.value = response.data!!.account.avatar.orEmpty()
                        stateIncognito.value = response.data?.account?.incognito!!
                        _listRequestAddFriend.tryEmit(response.data!!.account.friendRequests)
                        Log.e("dbt", response.data.toString())
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