package com.example.goingmerry.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.ui.theme.Teal200
import com.example.goingmerry.viewModel.AnonymousChatViewModel
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.LoginViewModel
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnonymousChat(loginViewModel: LoginViewModel, anonymousChatViewModel: AnonymousChatViewModel, nav: NavController, stateIncognito: Boolean){
    var messageTyping by rememberSaveable { mutableStateOf("") }

    val listIncognitoMessage by anonymousChatViewModel.listIncognitoMessage.collectAsState()

    val lenInputMessage = if(messageTyping == "") 4f else 7f
    if(stateIncognito){
        anonymousChatViewModel.stateAnonymousChat.value = true
    }
    Log.e("state1", anonymousChatViewModel.stateAnonymousChat.value.toString() + " " + anonymousChatViewModel.jobReceiver.toString())
    if(anonymousChatViewModel.stateAnonymousChat.value && ((anonymousChatViewModel.jobReceiver == null
        && anonymousChatViewModel.jobSend == null) ||
                (anonymousChatViewModel.jobReceiver!!.isCancelled && anonymousChatViewModel.jobSend!!.isCancelled))){
        Log.e("state", "true")
        anonymousChatViewModel.receiverMessages(loginViewModel)
        anonymousChatViewModel.sendMessages(loginViewModel)
    }else if (!anonymousChatViewModel.stateAnonymousChat.value){
        anonymousChatViewModel.jobReceiver?.cancel()
        anonymousChatViewModel.jobSend?.cancel()
    }

    Column {
        TopBarAnonymousChat(anonymousChatViewModel, loginViewModel, nav, stateIncognito)
        LazyColumn(
            modifier = Modifier.weight(9f),
            reverseLayout = true
        ){
            items(listIncognitoMessage.sortedBy {
                Instant.parse(it.sentAt).epochSecond.toInt()
            }.asReversed()){
                    message->
                IncognitoMessage(content = message.content, id = message.senderId)
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            if(messageTyping == ""){
                val addImage = Icons.Filled.Add
                Icon(
                    addImage,
                    contentDescription = "Chọn file đính kèm",
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .fillMaxSize(),
                    tint = MaterialTheme.colors.secondaryVariant
                )

                val img = Icons.Filled.Image
                Icon(
                    img,
                    contentDescription = "Chọn ảnh",
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .fillMaxSize(),
                    tint = MaterialTheme.colors.secondaryVariant
                )
            }
            TextField(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(lenInputMessage)
                    .clip(RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.onPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                value = messageTyping,
                onValueChange = {
                    messageTyping = it
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Nhắn tin",
                        color = Color.Black
                    )
                }
            )
            Button(
                onClick = {
                    if(messageTyping != "") {
                        anonymousChatViewModel.setContentSendMessage(messageTyping)
                        anonymousChatViewModel.setFlag(true)
                        messageTyping = ""
                    }
                },
                modifier = Modifier
                    .weight(1.5f)
                    .clip(CircleShape)
                    .padding(5.dp),
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
            ) {
                Text(
                    text = "GỬI"
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarAnonymousChat(anonymousChatViewModel: AnonymousChatViewModel, loginViewModel: LoginViewModel, nav: NavController, stateIncognito: Boolean){
    var switchOn by rememberSaveable {
        mutableStateOf(stateIncognito)
    }
    TopAppBar (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        title = {
            Image(
                painter = painterResource(R.drawable._unknown_person),
                contentDescription = "unknown_chat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 5.dp)
                    .clip(CircleShape)
            )
            Text(text = "CVNL")
        },
        navigationIcon = {
            val image = Icons.Filled.ArrowBack
            IconButton(onClick = {
                nav.navigate(Routes.Home.route){
                    launchSingleTop = true
                }
            }) {
                Icon(image, contentDescription = "Back to Home")
            }
        },
        actions = {
            IconButton(onClick = {
            }) {
                Switch(
                    checked = switchOn,
                    onCheckedChange = { switchOn_ ->
                        anonymousChatViewModel.setStateAnonymousChat(loginViewModel.token.value)
                        switchOn = switchOn_
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Teal200,
                        checkedTrackColor = Teal200
                    )
                )
            }
        }
    )
}

@Composable
fun IncognitoMessage(content: String, id: Long) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if(id == -1L) Arrangement.Start else Arrangement.End
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val backgroundBody = if(id != -1L) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.background
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = backgroundBody
            ) {
                Text(
                    text = content,
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .widthIn(0.dp, (screenWidth / 2)),
                    maxLines = Int.MAX_VALUE,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

