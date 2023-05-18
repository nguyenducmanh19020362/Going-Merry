package com.example.goingmerry.viewModel

import AddGroupMutation
import GetGroupsQuery
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.goingmerry.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import type.GroupInput
import type.GroupMemberInput

class GroupManagerViewModel: ViewModel() {
    var state = mutableStateOf(false)
    var error = mutableStateOf(0)

    private val _listGroups = MutableStateFlow(listOf<GetGroupsQuery.Group>())
    val listGroups = _listGroups.asStateFlow()

    private val _listFriends = MutableStateFlow(listOf<GetGroupsQuery.Friend>())
    val listFriend = _listFriends.asStateFlow()

    private val _idAccount = MutableStateFlow("")
    val idAccount = _idAccount.asStateFlow()

    private val _listChecks = MutableStateFlow<List<Boolean>>(emptyList())
    val listChecks = _listChecks.asStateFlow()

    fun getGroups(token: String){
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
                val users = apolloClient.query(GetGroupsQuery())
                users.enqueue(object: ApolloCall.Callback<GetGroupsQuery.Data>(){
                    override fun onResponse(response: Response<GetGroupsQuery.Data>) {
                        Log.e("data", response.data.toString())
                        _listGroups.tryEmit(response.data!!.account.groups)
                        _listFriends.tryEmit(response.data!!.account.friends)
                        _listChecks.tryEmit(List(response.data!!.account.friends.size){false})
                        _idAccount.tryEmit(response.data!!.account.id)
                        Log.e("data", response.data.toString())
                    }

                    override fun onFailure(e: ApolloException) {
                        Log.e("Todo", e.toString())
                    }
                })
            }catch (e: Exception){
                Log.e("error", e.toString())
            }
        }
    }
    fun createGroups(token: String, listPeople: List<GroupMemberInput>, nameGroup: String, idGroup: String){
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
                Log.e("group", AddGroupMutation(Input.fromNullable(GroupInput(Input.absent(), Input.fromNullable(nameGroup), Input.fromNullable(listPeople)))).toString())
                var users = apolloClient.mutate(AddGroupMutation(Input.fromNullable(GroupInput(Input.absent(), Input.fromNullable(nameGroup), Input.fromNullable(listPeople)))))
                if(idGroup != ""){
                    users = apolloClient.mutate(AddGroupMutation(Input.fromNullable(GroupInput(Input.fromNullable(idGroup), Input.fromNullable(nameGroup), Input.fromNullable(listPeople)))))
                }
                users.enqueue(object: ApolloCall.Callback<AddGroupMutation.Data>(){
                    override fun onResponse(response: Response<AddGroupMutation.Data>) {
                        if(response.data?.group == null){
                            error.value = 1
                        }else{
                            error.value = 2
                        }
                        Log.e("data", response.data.toString())
                    }

                    override fun onFailure(e: ApolloException) {
                        error.value = 1
                        Log.e("Todo", e.toString())
                    }
                })
            }catch (e: Exception){
                Log.e("error", e.toString())
            }
        }
    }
    fun checkAt(index: Int){
        viewModelScope.launch(Dispatchers.IO){
            val list = listChecks.value.toMutableList()
            if(index < list.size){
                list[index] = !list[index]
                _listChecks.emit(list.toList())
            }
        }
    }
    fun checkMember(listMembers: List<GetGroupsQuery.Member>){
        viewModelScope.launch(Dispatchers.IO){
            val list = listChecks.value.toMutableList()
            if(list.isNotEmpty()){
                for(member in listMembers){
                    for(fri in listFriend.value){
                        if(member.user?.id == fri.user.id){
                            list[listFriend.value.indexOf(fri)] = true;
                            break;
                        }
                    }
                }
            }
            _listChecks.emit(list.toList())
            state.value = true
        }
    }
}
