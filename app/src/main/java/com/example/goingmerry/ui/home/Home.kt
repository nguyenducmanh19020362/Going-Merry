package com.example.goingmerry.ui.home

import AccountQuery
import FindUsersQuery
import android.media.Image
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsApplications
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.goingmerry.R
import com.example.goingmerry.ScreenSizes
import com.example.goingmerry.TypeScreen
import com.example.goingmerry.URL
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.ui.ChatBox
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.DirectMessage
import com.example.goingmerry.viewModel.HomeViewModel
import com.example.goingmerry.viewModel.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenHome(model: LoginViewModel,chatBoxViewModel: ChatBoxViewModel, homeViewModel: HomeViewModel, nav: NavController){
    val listPeople by homeViewModel.listPeople.collectAsState()
    var wordSearch by rememberSaveable { mutableStateOf("") }
    var buttonSearch by rememberSaveable {
        mutableStateOf(false)
    }

    var typeList by rememberSaveable {
        mutableStateOf("Friend")
    }

    homeViewModel.account(model.token.value)
    if(chatBoxViewModel.stateSockets.value == "OFF"){
        chatBoxViewModel.receiverMessages(model, homeViewModel)
        chatBoxViewModel.sendMessages(model)
    }

    val conversations by homeViewModel.conversations.collectAsState()
    val directMessages by chatBoxViewModel.listReceiverMessage.collectAsState()

    if(homeViewModel.firstLogin.value){
        nav.navigate(Routes.FillInfo.route){
            popUpTo(Routes.Home.route){
                inclusive = true
            }
        }
        homeViewModel.firstLogin.value = false
    }
    if(homeViewModel.idAccount.value == ""){
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp),
                color = Color.White
            )
        }
    }
    Column (modifier = Modifier.fillMaxHeight()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(135.dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(MaterialTheme.colors.secondary)
        ) {
            Row {
                var size  = 40.dp
                var fonts = 30.sp
                if(ScreenSizes.type() == TypeScreen.Compat){
                    size = 30.dp
                    fonts = 20.sp
                }
                Image(
                    painter = painterResource(R.drawable.app_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Going Merry",
                    fontSize = fonts,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                TextField(
                    value = wordSearch,
                    onValueChange = {wordSearch = it},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.onPrimary
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .width((ScreenSizes.weight() / 3 * 2).dp),
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                )

                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "setting",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = {
                            nav.navigate(Routes.Setting.route) {
                                launchSingleTop = true
                            }
                        }
                        )
                )
            }
        }
        if(wordSearch == ""){
            BodyHome(conversations, directMessages, nav, homeViewModel.idAccount.value,typeList, changeTypeList = {type: String ->
                typeList = type
            }, model.token.value)
        }else{
            homeViewModel.findPeoples(wordSearch, model)
            ListPeople(listPeople, nav, model.token.value)
        }
    }
}

@Composable
fun BodyHome(conversations: List<AccountQuery.Conversation>, directMessages: List<DirectMessage>, nav: NavController, idAccount: String, typeList: String,
             changeTypeList: (type: String) -> Unit, token: String){
    val flag by rememberSaveable {
        mutableStateOf(true)
    }

    val colorButtonFriend = if(typeList == "Friend") MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.onSecondary
    val colorButtonGroup = if(typeList == "Group") MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.onSecondary

    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ){
            var size  = 40.dp
            if(ScreenSizes.type() == TypeScreen.Compat){
                size = 30.dp
            }
            Button(
                onClick = {changeTypeList("Friend")},
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = colorButtonFriend)
            ){
                Text(text = "Bạn bè")
            }
            Spacer(modifier = Modifier.width(40.dp))
            Image(
                painter = painterResource(id = R.drawable.app_icon), 
                contentDescription = "image",
                modifier = Modifier.size(size)
            )
            Spacer(modifier = Modifier.width(40.dp))
            Button(
                onClick = {changeTypeList("Group")},
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = colorButtonGroup),
                shape = RoundedCornerShape(10.dp)
            ){
                Text(text = "Nhóm")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colors.secondary)
                .padding(10.dp)
        ) {
            var size  = 50.dp
            var fonts = 20.sp
            if(ScreenSizes.type() == TypeScreen.Compat){
                size = 40.dp
                fonts = 15.sp
            }
            Text(
                fontSize = fonts,
                text = "Tin nhắn trực tiếp",
                modifier = Modifier.padding(5.dp)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = MaterialTheme.colors.onBackground)
                .padding(3.dp)
                .clickable {
                    nav.navigate(
                        Routes.AnonymousChat.route
                    )
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable._unknown_person),
                    contentDescription = "Ẩn danh",
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Trò chuyện ẩn danh",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "Trò chuyện cùng người lạ ẩn danh ngẫu nhiên")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(typeList == "Friend") {
                ListFriends(conversations, directMessages, nav, idAccount, token)
            }else{
                ListGroups(conversations, directMessages, nav, idAccount)
            }
        }
    }
}

/*@Composable
@Preview
fun ReviewBodyHome(){
    BodyHome()
}*/

@Composable
fun SearchForm(wordSearch: String, onValueChange: (String) -> Unit, model: LoginViewModel){
    TextField(
        modifier = Modifier
            .height(50.dp)
            .clip(RoundedCornerShape(15.dp))
            .padding(10.dp),
        value = wordSearch,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.onPrimary
        ),
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = "")},
        trailingIcon = {
            /*val image = Icons.Filled.Search
            IconButton(onClick = {buttonSearch = true}) {
                Icon(image, contentDescription = "IconSearch")
            }*/
        },
        singleLine = true
    )
}

/*
@Preview
@Composable
fun ReviewSearchForm(){
    SearchForm()
}*/

@Composable
fun ListFriends(listConversation: List<AccountQuery.Conversation>, directMessages: List<DirectMessage>, nav: NavController, idAccount: String, token: String){
    val imageLoader = ImageLoader(context = LocalContext.current)
    var str by rememberSaveable {
        mutableStateOf("")
    }

    LazyColumn(modifier = Modifier.fillMaxHeight()){
        if(listConversation.isNotEmpty()){
            items(listConversation.sortedBy {
                compare(directMessages, it)
            }.asReversed()){conversion->
                if(conversion.members.size == 2){
                    for(member in conversion.members){
                        //Log.e("id", member.id + " " + idAccount)
                        if(member.id != idAccount){
                            val mode = "${member.avatar}"
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    nav.navigate(
                                        Routes.ChatBox.route + "/${
                                            listConversation.indexOf(
                                                conversion
                                            )
                                        }"
                                    )
                                }
                            ) {
                                var size  = 50.dp
                                var fonts = 20.sp
                                var fonts1 = 17.sp
                                if(ScreenSizes.type() == TypeScreen.Compat){
                                    size = 40.dp
                                    fonts = 15.sp
                                    fonts1 = 12.sp
                                }
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("${URL.urlServer}${mode}")
                                        .setHeader("Authorization", "Bearer $token").build(),
                                    imageLoader = imageLoader,
                                    contentDescription = "Ẩn danh",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(size)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    var len = 0
                                    str = getLastMessage(directMessages, conversion)
                                    if(str != "") {
                                        len = str.length;
                                        if(len >= 20){
                                            len = 20;
                                        }
                                        str = str.subSequence(0, len).toString()
                                    }
                                    Text(
                                        text = member.name,
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = fonts
                                    )
                                    Text(
                                        text = str,
                                        style = MaterialTheme.typography.body2,
                                        fontSize = fonts1
                                    )
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListGroups(listConversation: List<AccountQuery.Conversation>, directMessages: List<DirectMessage>, nav: NavController, idAccount: String){
    var str by rememberSaveable {
        mutableStateOf("")
    }

    LazyColumn(modifier = Modifier.fillMaxHeight()){
        if(listConversation.isNotEmpty()){
            items(listConversation.sortedBy {
                compare(directMessages, it)
            }.reversed()){conversion->
                if(conversion.members.size > 2){
                    //Log.e("id", member.id + " " + idAccount)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            nav.navigate(
                                Routes.ChatBoxGroup.route + "/${
                                    listConversation.indexOf(
                                        conversion
                                    )
                                }"
                            )
                        }
                    ) {
                        var size  = 50.dp
                        var fonts = 20.sp
                        var fonts1 = 17.sp
                        if(ScreenSizes.type() == TypeScreen.Compat){
                            size = 40.dp
                            fonts = 15.sp
                            fonts1 = 12.sp
                        }
                        Image(
                            painter = painterResource(R.drawable.app_icon),
                            contentDescription = "Ẩn danh",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(size)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            var len = 0
                            str = getLastMessage(directMessages, conversion)
                            if(str != "") {
                                len = str.length;
                                if(len >= 20){
                                    len = 20;
                                }
                                str = str.subSequence(0, len).toString()
                            }
                            Text(
                                text = conversion.name,
                                style = MaterialTheme.typography.subtitle2,
                                fontSize = fonts
                            )
                            Text(
                                text = str,
                                style = MaterialTheme.typography.body2,
                                fontSize = fonts1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListPeople(listPeople: List<FindUsersQuery.FindUser>, nav: NavController, token: String){
    val imageLoader = ImageLoader(context = LocalContext.current)
    if(listPeople.isNotEmpty()){
        LazyColumn(modifier = Modifier.fillMaxHeight()){
            items(listPeople){people->
                var mode = "${people.avatar}"
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable {
                        nav.navigate(
                            Routes.Profile.route + "/${people.id}"
                        )
                    }
                ) {
                    var size  = 50.dp
                    var fonts = 20.sp
                    if(ScreenSizes.type() == TypeScreen.Compat){
                        size = 40.dp
                        fonts = 15.sp
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("${URL.urlServer}${mode}")
                            .setHeader("Authorization", "Bearer $token").build(),
                        imageLoader = imageLoader,
                        contentDescription = "Ẩn danh",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = people.name,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = fonts
                        )
                    }
                }
            }
        }
    }
}

fun compare(directMessages: List<DirectMessage>, conversation: AccountQuery.Conversation): Int{
    if(directMessages.isNotEmpty()){
        for(item in directMessages.reversed()){
            if(item.idConversation == conversation.id){
                return item.sendAt
            }
        }
    }else if(conversation.latestMessages.isNotEmpty()){
        return conversation.latestMessages.first().sendAt
    }
    return -1000
}

fun getLastMessage(directMessages: List<DirectMessage>, conversation: AccountQuery.Conversation): String{
    if(directMessages.isNotEmpty()){
        for(item in directMessages.reversed()){
            if(item.idConversation == conversation.id){
                return item.messageContent
            }
        }
    }
    if(conversation.latestMessages.isNotEmpty()){
        return conversation.latestMessages.first().content.toString()
    }
    return ""
}
