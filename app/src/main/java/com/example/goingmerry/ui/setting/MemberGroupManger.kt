package com.example.goingmerry.ui.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apollographql.apollo.api.Input
import com.example.goingmerry.URL
import com.example.goingmerry.viewModel.GroupManagerViewModel
import type.GroupMemberInput
import type.UserRole

@Composable
fun MemberGroupManager(listMembers: List<GetGroupsQuery.Member>, groupManagerViewModel: GroupManagerViewModel,
                       idGroup: String, nameGroup: String, token: String){
    val listFriend by groupManagerViewModel.listFriend.collectAsState()
    val listChecks by groupManagerViewModel.listChecks.collectAsState()
    if(listChecks.isNotEmpty() && !groupManagerViewModel.state.value){
        groupManagerViewModel.checkMember(listMembers)
    }
    val idAccount by groupManagerViewModel.idAccount.collectAsState()
    val imageLoader = ImageLoader(context = LocalContext.current)
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.secondary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Update thành viên: ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
        Text(
            text = "Chọn bạn bè tham gia group: ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(start = 5.dp)
        )
        Text(
            text = "(Ít nhất 3 người)",
            fontSize = 17.sp,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(modifier = Modifier.weight(2f)) {
            if (listFriend.isNotEmpty()) {
                items(listFriend) { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            modifier = Modifier.padding(
                                end = 10.dp,
                                top = 5.dp,
                                bottom = 5.dp,
                                start = 5.dp
                            ),
                            checked = listChecks[listFriend.indexOf(member)],
                            onCheckedChange = {
                                groupManagerViewModel.checkAt(listFriend.indexOf(member))
                            }
                        )
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("${URL.urlServer}${member.avatar}")
                                .setHeader("Authorization", "Bearer $token").build(),
                            imageLoader = imageLoader,
                            contentDescription = "Ẩn danh",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Update",
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable {
                            var list = listOf<GroupMemberInput>()
                            for (fri in listFriend) {
                                if (listChecks[listFriend.indexOf(fri)]) {
                                    val member = GroupMemberInput(
                                        Input.fromNullable(fri.id),
                                        Input.fromNullable(UserRole.MEMBER)
                                    )
                                    list = list + member
                                }
                            }
                            val member = GroupMemberInput(
                                Input.fromNullable(idAccount),
                                Input.fromNullable(UserRole.MANAGER)
                            )
                            list = list + member
                            groupManagerViewModel.createGroups(token, list, nameGroup, idGroup)
                        },
                    fontSize = 20.sp,
                )
            }
        }

    }
}

