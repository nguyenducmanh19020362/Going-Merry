package com.example.goingmerry.ui

import android.graphics.Paint
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goingmerry.R

@Composable
fun ChatBox(){
    var messageTyping by rememberSaveable { mutableStateOf("") }
    var lenInputMessage = if(messageTyping == "") 4f else 7f
    Column {
        TopBar()
        LazyColumn(
            modifier = Modifier.weight(9f),
            reverseLayout = true
        ){
            items(DataChatBox.listMessage){
                    message->
                MessageCard(msg = message)
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
                onClick = { /*TODO*/ },
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

@Preview
@Composable
fun PreviewBoxChat(){
    ChatBox()
}

@Composable
fun InputMessage(){

}

@Composable
@Preview
fun PreviewInputMessage(){
    InputMessage()
}

@Composable
fun TopBar(){
    TopAppBar (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        title = {
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = "Friend",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 5.dp)
                    .clip(CircleShape)
            )
            Text(text = "Taylor Swift")
        },
        navigationIcon = {
            val image = Icons.Filled.ArrowBack
            IconButton(onClick = { /*TODO*/ }) {
                Icon(image, contentDescription = "Back to Home")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "To Profile",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}

@Composable
fun MessageCard(msg: Message) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if(msg.author != "me") Arrangement.Start else Arrangement.End
    ) {
        if(msg.author != "me"){
            Image(
                painter = painterResource(id = R.drawable.img),
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
            val backgroundBody = if(msg.author == "me") MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.background
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = backgroundBody
            ) {
                Text(
                    text = msg.body,
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

