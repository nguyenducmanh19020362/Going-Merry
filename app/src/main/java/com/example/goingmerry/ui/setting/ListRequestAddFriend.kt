package com.example.goingmerry.ui.setting

import AccountQuery
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListRequestAddFriends(listFriendRequest: List<AccountQuery.FriendRequest>){
    LazyColumn{
        items(listFriendRequest){
            Column() {
                Text(text = "${it.sender!!.name} muốn trò chuyện với bạn", fontSize = 10.sp)
                Row (){
                    Row(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colors.secondaryVariant)
                    ){
                        Text(text = "Đồng ý", fontSize = 5.sp, modifier = Modifier.padding(2.dp))
                    }
                    Row(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colors.secondaryVariant)
                    ){
                        Text(text = "Từ chối", fontSize = 5.sp, modifier = Modifier.padding(2.dp))
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
    ListRequestAddFriends(listOf())
}