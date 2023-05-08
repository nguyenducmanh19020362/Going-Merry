package com.example.goingmerry.ui.signInSignUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.goingmerry.URL

@Composable
fun ListMembers(members: List<AccountQuery.Member>, token: String){
    val imageLoader = ImageLoader(context = LocalContext.current)
    Column (modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.secondary)
        ){
        Text(
            text = "Danh sách Thành viên:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(modifier = Modifier.weight(1f)){
            if(members.isNotEmpty()){
                items(members){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("${URL.urlServer}${it.avatar}")
                                .setHeader("Authorization", "Bearer $token").build(),
                            imageLoader = imageLoader,
                            contentDescription = "avatar",
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
@Composable
@Preview
fun PreviewListMember(){
    ListMembers(listOf(), "")
}