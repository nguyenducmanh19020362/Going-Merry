package com.example.goingmerry.viewModel

import AddFriendMutation
import DeleteFriendMutation
import UserProfileQuery
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class ProfileViewModel: ViewModel(){
    var name = mutableStateOf("")
    var age = mutableStateOf("")
    var job = mutableStateOf("")
    var gender = mutableStateOf("")
    var address = mutableStateOf("")
    var avatar = mutableStateOf("")
    var favorites = mutableStateOf("")

    var idDeleteFriend = mutableStateOf("");
    fun matchProfiles(id: String, token: String){
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
                    .serverUrl("${URL.urlServer}/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.query(UserProfileQuery(id))
                users.enqueue(object: ApolloCall.Callback<UserProfileQuery.Data>(){
                    override fun onResponse(response: Response<UserProfileQuery.Data>) {
                        name.value = response.data!!.user.name
                        age.value = response.data!!.user.birthday.toString()
                        job.value = response.data!!.user.job.toString()
                        gender.value = response.data!!.user.gender.toString()
                        address.value = response.data!!.user.address.toString()
                        avatar.value = response.data!!.user.avatar.toString()
                        favorites.value = response.data!!.user.favorites.toString()
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
    fun addFriend(id: String, token: String){
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
                    .serverUrl("${URL.urlServer}/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.mutate(AddFriendMutation(id))
                users.enqueue(object: ApolloCall.Callback<AddFriendMutation.Data>(){
                    override fun onResponse(response: Response<AddFriendMutation.Data>) {
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

    fun deleteFriend(id: String, token: String){
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
                    .serverUrl("${URL.urlServer}/graphql")
                    .okHttpClient(okHttp)
                    .build()
                val users = apolloClient.mutate(DeleteFriendMutation(id))
                users.enqueue(object: ApolloCall.Callback<DeleteFriendMutation.Data>(){
                    override fun onResponse(response: Response<DeleteFriendMutation.Data>) {
                        idDeleteFriend.value = response.data?.unfriend?.id.toString()
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
}

