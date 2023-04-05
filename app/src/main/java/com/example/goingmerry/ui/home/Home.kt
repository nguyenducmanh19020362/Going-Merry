package com.example.goingmerry.ui.home

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.goingmerry.R
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.HomeViewModel
import com.example.goingmerry.viewModel.LoginViewModel

@Composable
fun ScreenHome(navController: NavController, model: LoginViewModel){
    Column (modifier = Modifier.fillMaxHeight()){
        var wordSearch by rememberSaveable { mutableStateOf("") }
        var buttonSearch by rememberSaveable {
            mutableStateOf(false)
        }

        LogoHome()
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            SearchForm(wordSearch, onValueChange = {wordSearch = it}, model)

            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "profile",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = {
                        navController.navigate(Routes.Setting.route) {
                            launchSingleTop = true
                        }
                    })
            )
        }
        BodyHome()
    }
}

/*@Composable
@Preview
fun ReviewScreenHome(){
    ScreenHome()
}*/
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
    }
}

/*@Preview
@Composable
fun ReviewLogoHome(){
    LogoHome()
}*/

@Composable
fun BodyHome(){
    val flag by rememberSaveable {
        mutableStateOf(true)
    }

    val colorButtonFriend = if(flag) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.onSecondary
    val colorButtonGroup = if(!flag) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.onSecondary

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
                    .buttonColors(backgroundColor = colorButtonFriend)
            ){
                Text(text = "Bạn bè")
            }
            Spacer(modifier = Modifier.width(50.dp))
            Image(
                painter = painterResource(id = R.drawable.app_icon), 
                contentDescription = "image"
            )
            Spacer(modifier = Modifier.width(50.dp))
            Button(
                onClick = {},
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
                        .size(50.dp)
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
fun SearchForm(wordSearch: String, onValueChange: (String) -> Unit, model: LoginViewModel){
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
            onValueChange
            HomeViewModel.findPeoples(wordSearch, model)
            HomeViewModel.findPeoples("a", model)
            HomeViewModel.findPeoples("b", model)
            HomeViewModel.findPeoples("c", model)
            HomeViewModel.findPeoples("d", model)
        },
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
fun ListFriends(){
    LazyColumn(modifier = Modifier.fillMaxHeight()){
        items(ListFriends.listFriends){friend->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = friend.linkImageAvatar),
                    contentDescription = "Ẩn danh",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = friend.nameUser,
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 20.sp
                    )
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
