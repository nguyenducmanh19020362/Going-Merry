package com.example.goingmerry.ui.home

import com.example.goingmerry.R

object ListFriends {
     var listFriends = listOf(
         Friend(R.drawable.img, "TayLor Swift"),
         Friend(R.drawable.img, "TayLor Swift"),
         Friend(R.drawable.img, "TayLor Swift"),
         Friend(R.drawable.img, "TayLor Swift"),
         Friend(R.drawable.img, "TayLor Swift")
     )
 }

data class Friend(
    val linkImageAvatar: Int,
    val nameUser: String
)