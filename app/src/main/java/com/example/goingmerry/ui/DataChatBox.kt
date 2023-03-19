package com.example.goingmerry.ui

object DataChatBox {
    val listMessage = listOf(
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjkiiiiiiiiiiiiii", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmc", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "me"),
        Message("hellosfjoasjfsoiajfsjxkmco", "me"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "me"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "me"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjkiiiiiiiiiiiiii", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmc", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "TayLor Swift"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "me"),
        Message("hellosfjoasjfsoiajfsjxkmco", "me"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "me"),
        Message("hellosfjoasjfsoiajfsjxkmcowijfasjdfsljalsdjoxjk", "me"),
    )
}

data class Message(
    val body: String,
    val author: String
)