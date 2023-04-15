package com.example.goingmerry.ui.setting

import AccountQuery
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apollographql.apollo.api.Input
import com.example.goingmerry.viewModel.ListRAFViewModel
import type.ConversationInput
import type.FriendRequestReply

@Composable
fun ListRequestAddFriends(token: String, listFriendRequest: List<AccountQuery.FriendRequest>, listRAFViewModel: ListRAFViewModel){
    var newList by rememberSaveable {
        mutableStateOf(listFriendRequest.toMutableList())
    }
    var idSender by rememberSaveable {
        mutableStateOf("")
    }
    var idReceiver by rememberSaveable {
        mutableStateOf("")
    }
    val res by listRAFViewModel.res.collectAsState()
    if (res != "") {
        listRAFViewModel.createConversation(
            token, ConversationInput(
                Input.absent(),
                Input.fromNullable("Conversation"),
                Input.fromNullable(
                    listOf(
                        idSender,
                        idReceiver
                    )
                ),
                Input.absent(),
                Input.absent(),
            )
        )
        listRAFViewModel.resetRes()
    }
    LazyColumn{
        items(newList){
            Column() {
                Text(text = "${it.sender!!.name} muốn trò chuyện với bạn")
                Row (){
                    Row(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colors.secondaryVariant)
                            .clickable {
                                listRAFViewModel.replyRequestAddFriend(
                                    token,
                                    it.sender.id,
                                    FriendRequestReply.ACCEPT
                                )
                                idSender = it.sender.id
                                idReceiver = it.receiver!!.id
                                newList.remove(it)
                            }
                    ){
                        Text(text = "Đồng ý", modifier = Modifier.padding(2.dp))
                    }
                    Row(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colors.secondaryVariant)
                            .clickable {
                                listRAFViewModel.replyRequestAddFriend(
                                    token,
                                    it.sender.id,
                                    FriendRequestReply.DENY
                                )
                                newList.remove(it)
                            }
                    ){
                        Text(text = "Từ chối", modifier = Modifier.padding(2.dp))
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
@Preview
fun PreviewListRequestAddFriends(){
   // ListRequestAddFriends(listOf())
}