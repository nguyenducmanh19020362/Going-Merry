package com.example.goingmerry.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goingmerry.R

@Composable
fun ScreenHome(){
    Column (modifier = Modifier.fillMaxHeight()){
        LogoHome()
        BodyHome()
    }
}

@Composable
@Preview
fun ReviewScreenHome(){
    ScreenHome()
}
@Composable
fun LogoHome(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colors.secondary)
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Going Merry",
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically
                ){
            SearchForm()
            
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "profile",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

/*@Preview
@Composable
fun ReviewLogoHome(){
    LogoHome()
}*/

@Composable
fun BodyHome(){
    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ){
                Text(text = "Bạn bè")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.app_icon), 
                contentDescription = "image"
            )
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
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
            Text(
                fontSize = 20.sp,
                text = "Tin nhắn trực tiếp",
                modifier = Modifier.padding(5.dp)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = MaterialTheme.colors.onBackground)
                .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "Ẩn danh",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Column {
                    Text(text = "Trò chuyện ẩn danh")
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "Trò chuyện cùng người lạ ẩn danh ngẫu nhiên")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ListFriends()
        }
    }
}

/*@Composable
@Preview
fun ReviewBodyHome(){
    BodyHome()
}*/

@Composable
fun SearchForm(){
    var wordSearch by rememberSaveable { mutableStateOf("") }
    var buttonSearch by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        modifier = Modifier
            .height(50.dp)
            .clip(RoundedCornerShape(15.dp))
            .padding(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.onPrimary
        ),
        shape = RoundedCornerShape(10.dp),
        value = wordSearch,
        onValueChange = {
            wordSearch = it
        },
        label = { Text(text = "")},
        trailingIcon = {
            val image = Icons.Filled.Search
            IconButton(onClick = {buttonSearch = true}) {
                Icon(image, contentDescription = "IconSearch")
            }
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
fun ListFriends(){
    LazyColumn(modifier = Modifier.fillMaxHeight()){
        items(ListFriends.listFriends){friend->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = friend.linkImageAvatar),
                    contentDescription = "Ẩn danh",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Column {
                    Text(text = friend.nameUser)
                }
            }
        }
    }
}

/*@Composable
@Preview
fun ReviewListFriends(){
    ListFriends()
}*/
