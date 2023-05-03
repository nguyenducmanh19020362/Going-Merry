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
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.ReceiverMessage
import com.example.goingmerry.viewModel.SendMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import java.lang.reflect.Member

@Composable
fun ChatBoxGroup(conversation: AccountQuery.Conversation, chatBoxViewModel: ChatBoxViewModel, id: String,
                 navController: NavController, token: String){
    Log.e("idConversation", conversation.id)
    chatBoxViewModel.conversationId.value = conversation.id.toLong()
    var messageTyping by rememberSaveable { mutableStateOf("") }
    val messages by rememberSaveable {
        mutableStateOf(conversation.latestMessages)
    }

    val directMessages by chatBoxViewModel.listReceiverMessage.collectAsState()

    var beforeMessage by rememberSaveable {
        mutableStateOf(listOf<BeforeMessageQuery.BeforeMessage>())
    }

    val lenInputMessage = if(messageTyping == "") 4f else 7f

    var nameUser by rememberSaveable {
        mutableStateOf("")
    }
    val progressBar by chatBoxViewModel.progressBar.collectAsState()
    Column {
        for(member in conversation.members){
            if(id == member.id){
                nameUser = member.name
            }
        }
        TopBarGroup(conversation.name, navController ,conversation.id)
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
                var avatar = ""
                if(conversation.id == message.idConversation){
                    for(member in conversation.members){
                        if(member.id == message.idSender){
                            avatar = member.avatar.toString()
                            break;
                        }
                    }
                    MessageCard(msg = Message(message.idSender, message.messageContent, message.messageName), url = avatar, id)
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
                MessageCard(msg = Message(message.sender!!.id, message.content, message.sender.name), avatar, id)
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
                MessageCard(msg = Message(message.sender!!.id, message.content, message.sender.name), avatar, id)
            }
            item{
                LaunchedEffect(true) {
                    if(messages.isNotEmpty()){
                        var idMessage: String = messages.last().id
                        if(beforeMessage.isNotEmpty()){
                            Log.e("IdMessage", idMessage)
                            idMessage = beforeMessage.last().id
                        }
                        if(!progressBar){
                            chatBoxViewModel.setProgressBar(true)
                            chatBoxViewModel.getBeforeMessage(token, conversation.id, idMessage)
                        }
                        if(chatBoxViewModel.beforeMessages.value.isNotEmpty()){
                            beforeMessage = beforeMessage + chatBoxViewModel.beforeMessages.value
                            Log.e("error", beforeMessage.size.toString())
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
fun TopBarGroup(nameGroup: String, navController: NavController, idConversation: String){
    TopAppBar (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        title = {
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "Group",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 5.dp)
                    .clip(CircleShape)
            )
            Text(text = nameGroup)
        },
        navigationIcon = {
            val image = Icons.Filled.ArrowBack
            IconButton(onClick = { /*TODO*/ }) {
                Icon(image, contentDescription = "Back to Home")
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(
                    Routes.GroupMember.route + "/${
                        idConversation
                    }"
                )
            }) {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = "To Profile",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}
