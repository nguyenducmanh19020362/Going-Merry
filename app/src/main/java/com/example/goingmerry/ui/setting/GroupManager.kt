package com.example.goingmerry.ui.setting

import GetGroupsQuery
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apollographql.apollo.api.Input
import com.example.goingmerry.R
import com.example.goingmerry.URL
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.GroupManagerViewModel
import type.GroupMemberInput
import type.UserRole

@Composable
fun GroupManager(groupManagerViewModel: GroupManagerViewModel, token: String, nav: NavController){
    groupManagerViewModel.getGroups(token)
    val listGroup by groupManagerViewModel.listGroups.collectAsState()
    val listFriend by groupManagerViewModel.listFriend.collectAsState()
    val idAccount by groupManagerViewModel.idAccount.collectAsState()

    val predicate2: (GetGroupsQuery.Member) -> Boolean = {it.role == UserRole.MANAGER && it.user != null && it.user.id == idAccount}

    var addGroupClick by rememberSaveable {
        mutableStateOf(false)
    }

    val listChecks by groupManagerViewModel.listChecks.collectAsState()

    var nameGroup by rememberSaveable {
        mutableStateOf("")
    }

    var showDialog by rememberSaveable{
        mutableStateOf(0)
    }

    val imageLoader = ImageLoader(context = LocalContext.current)

    if(showDialog == 1){
        ShowDialog("Không đủ số lượng thành viên", "lỗi", exchangeShowDialog = {showDialog = 0})
    }

    if(showDialog == 2){
        ShowDialog("Chưa nhập tên nhóm", "lỗi", exchangeShowDialog = {showDialog = 0})
    }

    if(groupManagerViewModel.error.value == 1){
        ShowDialog(contentLog = "Tạo Group thất bại. Bạn hãy làm lại", "lỗi") {
            groupManagerViewModel.error.value = 0
        }
    }

    if(groupManagerViewModel.error.value == 2){
        ShowDialog(contentLog = "Tạo thành công", title = "Xác nhận") {
            groupManagerViewModel.error.value = 0
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.secondary)
    ) {
        val contentScreen = "Quản lý nhóm"
        TopBar(nav = nav, content = contentScreen)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Tạo nhóm mới: ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(start = 5.dp)
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "profile",
                Modifier
                    .background(Color.Gray)
                    .clickable { addGroupClick = !addGroupClick }
            )
        }
        if(addGroupClick){
            Text(
                text = "Nhập tên nhóm:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
            )
            TextField(
                value = nameGroup,
                onValueChange = {nameGroup = it},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.onPrimary
                ),
                modifier = Modifier
                    .padding(5.dp),
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle.Default.copy(fontSize = 18.sp)
            )
            if(nameGroup == ""){
                Text(
                    text = "Trường tên nhóm là bắt buộc",
                    fontSize = 15.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(start = 5.dp, bottom = 10.dp)
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
            LazyColumn(modifier = Modifier.weight(2f)){
                if(listFriend.isNotEmpty()){
                    items(listFriend){ friend->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                modifier = Modifier.padding(end = 10.dp, top = 5.dp, bottom = 5.dp, start = 5.dp),
                                checked = listChecks[listFriend.indexOf(friend)],
                                onCheckedChange = {groupManagerViewModel.checkAt(listFriend.indexOf(friend))
                                }
                            )
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("${URL.urlServer}${friend.user.avatar}")
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
                                text = friend.user.name,
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
            ){
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colors.secondaryVariant),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Tạo",
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                var list = listOf<GroupMemberInput>()
                                for (fri in listFriend) {
                                    if (listChecks[listFriend.indexOf(fri)]) {
                                        val member = GroupMemberInput(
                                            Input.fromNullable(fri.user.id),
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
                                if (nameGroup == "") {
                                    showDialog = 2
                                } else if (list.size < 3) {
                                    showDialog = 1
                                } else {
                                    groupManagerViewModel.createGroups(token, list, nameGroup, "")
                                }
                            },
                        fontSize = 20.sp,
                    )
                }
            }
        }
        Text(
            text = "Danh sách Nhóm quản lý:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(start = 5.dp)
        )
        LazyColumn(modifier = Modifier.weight(1f)){
            if(listGroup.isNotEmpty()){
                items(listGroup){
                    if(it.members.any(predicate2)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    nav.navigate(
                                        Routes.GroupMemberManager.route + "/${
                                            it.id
                                        }"
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.app_icon),
                                contentDescription = "Ẩn danh",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.subtitle2,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun Preview1(){
    //GroupManager(groupManagerViewModel = GroupManagerViewModel(), "")
}

@Composable
fun ShowDialog(contentLog: String, title: String, exchangeShowDialog: () -> Unit){
    AlertDialog(
        onDismissRequest = { exchangeShowDialog() },
        title = { Text(text = title) },
        text = { Text(text = contentLog) },
        confirmButton = {
            TextButton(onClick = {
                exchangeShowDialog()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                exchangeShowDialog()
            }) {
                Text("Cancel")
            }
        }
    )
}
