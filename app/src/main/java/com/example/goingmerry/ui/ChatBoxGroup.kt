package com.example.goingmerry.ui

import AccountQuery
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.goingmerry.IconChats
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.ui.signInSignUp.encodeFile
import com.example.goingmerry.ui.signInSignUp.getLenNameFile
import com.example.goingmerry.ui.signInSignUp.getNameFile
import com.example.goingmerry.viewModel.ChatBoxViewModel
import com.example.goingmerry.viewModel.MessageType
import com.example.goingmerry.viewModel.ReceiverMessage
import com.example.goingmerry.viewModel.SendMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import java.lang.reflect.Member

@Composable
fun ChatBoxGroup(conversation: AccountQuery.Conversation, chatBoxViewModel: ChatBoxViewModel, id: String,
                 navController: NavController, token: String){
    chatBoxViewModel.conversationId.value = conversation.id.toLong()
    var messageTyping by rememberSaveable { mutableStateOf("") }
    val messages by remember {
        mutableStateOf(conversation.latestMessages)
    }

    val directMessages by chatBoxViewModel.listReceiverMessage.collectAsState()

    var beforeMessage by remember {
        mutableStateOf(listOf<BeforeMessageQuery.BeforeMessage>())
    }

    val lenInputMessage = if(messageTyping == "") 3.5f else 6f

    var nameUser by rememberSaveable {
        mutableStateOf("")
    }

    var uri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }

    var selectIcon by remember {
        mutableStateOf(false)
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if(uriList.isNotEmpty()){
                uri = uriList[0]
            }
        }

    if(uri != Uri.EMPTY){
        val fileName = getNameFile(uri = uri)
        val lenFileName = getLenNameFile(fileName)
        val newAvatar = "$lenFileName$fileName;${encodeFile(uri)}"
        chatBoxViewModel.conversationId.value = conversation.id.toLong()
        chatBoxViewModel.contentSendMessage.value = newAvatar
        chatBoxViewModel.typeMessage.value = MessageType.IMAGE
        chatBoxViewModel.flag.value = true
        uri = Uri.EMPTY
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
                    if(message.messageType == MessageType.TEXT){
                        if(message.messageContent in IconChats.keys){
                            IconChats.icons[message.messageContent]?.let {
                                IconCard(icon = it, avatar = avatar, id = id, senderId = message.idSender, token = token)
                            }
                        }else{
                            MessageCard(msg = Message(message.idSender, message.messageContent, message.messageName),
                                url = avatar, id, token)
                        }
                    }else{
                        ImageCard(message.messageContent, avatar, id, message.idSender, token)
                    }
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
                if(message.type == type.MessageType.TEXT){
                    if(message.content in IconChats.keys){
                        IconChats.icons[message.content]
                            ?.let { IconCard(icon = it, avatar = avatar, id = id, senderId = message.sender?.id.toString(), token = token) }
                    }else {
                        MessageCard(
                            msg = Message(
                                message.sender!!.id,
                                message.content,
                                message.sender.name
                            ),
                            avatar, id, token
                        )
                    }
                }else{
                    ImageCard(message.content.toString(), avatar, id, message.sender?.id.toString(), token)
                }
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
                if(message.type == type.MessageType.TEXT){
                    if(message.content in IconChats.keys){
                        IconChats.icons[message.content]
                            ?.let { IconCard(icon = it, avatar = avatar, id = id, senderId = message.sender?.id.toString(), token = token) }
                    }else {
                        MessageCard(
                            msg = Message(
                                message.sender!!.id,
                                message.content,
                                message.sender.name
                            ),
                            avatar, id, token
                        )
                    }
                }else{
                    ImageCard(message.content.toString(), avatar, id, message.sender?.id.toString(), token)
                }
            }
            item{
                LaunchedEffect(true) {
                    if(messages.size > 20){
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
        if(selectIcon){
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                LazyRow(modifier = Modifier.padding(start = 5.dp)){
                    items(IconChats.keys){ key ->
                        IconChats.icons[key]?.let { icon ->
                            Icon(
                                icon,
                                contentDescription = "icon",
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clip(CircleShape)
                                    .fillMaxSize()
                                    .clickable {
                                        chatBoxViewModel.conversationId.value =
                                            conversation.id.toLong()
                                        chatBoxViewModel.typeMessage.value = MessageType.TEXT
                                        chatBoxViewModel.contentSendMessage.value = key
                                        chatBoxViewModel.flag.value = true
                                        selectIcon = false
                                    },
                                tint = MaterialTheme.colors.secondaryVariant
                            )
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
                val addImage = Icons.Filled.EmojiEmotions
                Icon(
                    addImage,
                    contentDescription = "Chọn Icon",
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .fillMaxSize()
                        .clickable {
                            selectIcon = !selectIcon
                        },
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
                        .fillMaxSize()
                        .clickable {
                            galleryLauncher.launch("image/*")
                        },
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
                        chatBoxViewModel.typeMessage.value = MessageType.TEXT
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
            IconButton(onClick = {
                navController.navigate(Routes.Home.route){
                    launchSingleTop = true
                }
            }) {
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
