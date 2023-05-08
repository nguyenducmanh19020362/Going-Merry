package com.example.goingmerry.ui

import AccountQuery
import android.graphics.Paint
import android.util.Log
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.goingmerry.R
import com.example.goingmerry.URL
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.ReceiverMessage
import com.example.goingmerry.viewModel.SendMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import java.lang.reflect.Member

@Composable
fun ChatBox(conversation: AccountQuery.Conversation, chatBoxViewModel: ChatBoxViewModel, nav: NavController, id: String, token: String){
    chatBoxViewModel.conversationId.value = conversation.id.toLong()
    var messageTyping by rememberSaveable { mutableStateOf("") }

    var messages by rememberSaveable {
        mutableStateOf(conversation.latestMessages)
    }

    var beforeMessage by rememberSaveable {
        mutableStateOf(listOf<BeforeMessageQuery.BeforeMessage>())
    }

    val directMessages by chatBoxViewModel.listReceiverMessage.collectAsState()

    val lenInputMessage = if(messageTyping == "") 4f else 7f

    var nameUser by rememberSaveable {
        mutableStateOf("")
    }

    val progressBar by chatBoxViewModel.progressBar.collectAsState()

    Column {
        for(member in conversation.members){
            if(member.id != id){
                TopBar(member, nav)
            }else{
                nameUser = member.name
            }
        }
        if(progressBar){
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
            }
        }
        LazyColumn(
            modifier = Modifier.weight(9f),
            reverseLayout = true
        ){
            items(directMessages.sortedBy {
                it.sendAt
            }.asReversed()){
                message->
                var avatar = "";
                if(conversation.id == message.idConversation){
                    for(member in conversation.members){
                        if(member.id == message.idSender){
                            avatar = member.avatar.toString()
                            break;
                        }
                    }
                    MessageCard(msg = Message(message.idSender, message.messageContent, message.messageName), url = avatar, id, token)
                }
            }
            items(messages.sortedBy {
                it.sendAt
            }.asReversed()){
                    message->
                var avatar = "";
                for(member in conversation.members){
                    if(message.sender!!.id == member.id){
                        avatar = member.avatar.toString()
                        break;
                    }
                }
                MessageCard(msg = Message(message.sender!!.id, message.content, message.sender.name), avatar, id, token)
            }
            items(beforeMessage.sortedBy {
                it.sendAt
            }.asReversed()){
                    message->
                var avatar = "";
                for(member in conversation.members){
                    if(message.sender!!.id == member.id){
                        avatar = member.avatar.toString()
                        break;
                    }
                }
                MessageCard(msg = Message(message.sender!!.id, message.content, message.sender.name), avatar, id, token)
            }
            item{
                LaunchedEffect(key1 = null) {
                    if(messages.isNotEmpty()){
                        var idMessage: String = messages.last().id
                        if(beforeMessage.isNotEmpty()){
                            idMessage = beforeMessage.last().id
                            Log.e("IdMessage", idMessage)
                        }
                        if(!progressBar){
                            chatBoxViewModel.setProgressBar(true)
                            chatBoxViewModel.getBeforeMessage(token, conversation.id, idMessage)
                        }
                        if(chatBoxViewModel.beforeMessages.value.isNotEmpty()){
                            beforeMessage = beforeMessage + chatBoxViewModel.beforeMessages.value
                            chatBoxViewModel.resetBeforeMessage()
                            chatBoxViewModel.setProgressBar(false)
                        }
                    }
                }
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
                        chatBoxViewModel.conversationId.value = conversation.id.toLong()
                        chatBoxViewModel.contentSendMessage.value = messageTyping
                        chatBoxViewModel.flag.value = true
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

@Composable
fun TopBar(member: AccountQuery.Member, nav: NavController){
    val imageLoader = ImageLoader(context = LocalContext.current)
    TopAppBar (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        title = {
            AsyncImage(
                model = "${URL.urlServer}${member.avatar}",
                imageLoader = imageLoader,
                contentDescription = "Friend",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 5.dp)
                    .clip(CircleShape)
            )
            Text(text = member.name)
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "To Profile",
                    modifier = Modifier.size(30.dp)
                        .clickable {
                            nav.navigate(Routes.Profile.route + "/${member.id}"){
                                launchSingleTop = true
                            }
                        }
                )
            }
        }
    )
}

@Composable
fun MessageCard(msg: Message, url: String, id: String, token: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val imageLoader = ImageLoader(LocalContext.current)
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if(id != msg.idMember) Arrangement.Start else Arrangement.End
    ) {
        if(id != msg.idMember){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${URL.urlServer}${url}")
                    .setHeader("Authorization", "Bearer $token").build(),
                imageLoader = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val backgroundBody = if(id == msg.idMember) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.background
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = backgroundBody
            ) {
                Text(
                    text = msg.content.toString(),
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

data class Message(
    val idMember: String,
    val content: String?,
    val author: String
)

