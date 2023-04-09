package com.example.goingmerry.viewModel

import AccountQuery
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.netty.buffer.ByteBufAllocator
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.emitOrClose
import io.rsocket.kotlin.ktor.client.RSocketSupport
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.payload.metadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import type.MessageState
import java.time.Instant
import java.util.List


class ChatBoxViewModel: ViewModel() {
    var listMessage = MutableStateFlow(listOf<Any>())
    var number: Long = 0
    var conversationId = mutableStateOf(number)
    private var listSendMessage = MutableStateFlow(listOf<SendMessage>())
    private val _listReceiverMessage = MutableStateFlow(mutableListOf<AccountQuery.Message>())
    val listReceiverMessage = _listReceiverMessage.asStateFlow()

    var contentSendMessage = mutableStateOf("")
    var flag = mutableStateOf(false)
    @RequiresApi(Build.VERSION_CODES.O)
    fun receiverMessages(token: String, homeViewModel: HomeViewModel){
        viewModelScope.launch (Dispatchers.Main){
            val client = HttpClient (CIO){ //create and configure ktor client
                install(WebSockets)
                install(RSocketSupport)
            }
            /*val rSocket: RSocket = client.rSocket(path = "rsocket/api.v1.messages.stream", host = "10.0.2.2", port = 8080)
            val metadata = ByteBufAllocator.DEFAULT.compositeBuffer()
            val routingMetadata: RoutingMetadata = TaggingMetadataCodec.createRoutingMetadata(
                ByteBufAllocator.DEFAULT,
                List.of("/route")
            )
            CompositeMetadataCodec.encodeAndAddMetadata(
                metadata,
                ByteBufAllocator.DEFAULT,
                MESSAGE_RSOCKET_ROUTING,
                routingMetadata.getContent()
            )*/
            val stream: Flow<Payload> = rSocket.requestStream(
                buildPayload {
                    this.metadata("message/x.rsocket.authentication.bearer.v0: Bearer $token")
                }
            )
            stream.collect { payload: Payload ->
                val json = payload.data.readText()
                val gson = Gson()
                val receiverMessage = gson.fromJson(json, ReceiverMessage::class.java)
                for(item in homeViewModel.conversations.value){
                    if(item.id.toLong() == receiverMessage.conversationId){
                        var name = ""
                        for(member in item.members){
                            if(member.id.toLong() == receiverMessage.senderId){
                                name = member.name
                            }
                        }
                        val newReceiverMessage = AccountQuery.Message("Message", item.id,
                            AccountQuery.Sender("Sender", receiverMessage.senderId.toString(), name ),
                            receiverMessage.content,
                            type.MessageType.TEXT,
                            MessageState.SENT
                        )
                        listReceiverMessage.value.add(newReceiverMessage)
                    }

                }/*
                listReceiverMessage.value.plus(receiverMessage)
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val createdAt = LocalDateTime.ofInstant(receiverMessage.createdAt, ZoneId.systemDefault()).format(format)
                val updatedAt = LocalDateTime.ofInstant(receiverMessage.updatedAt, ZoneId.systemDefault()).format(format)
                val deletedAt = LocalDateTime.ofInstant(receiverMessage.deletedAt, ZoneId.systemDefault()).format(format)
                println("$createdAt $updatedAt $deletedAt")*/
            }
        }
    }
    fun sendMessages(token:String){
        viewModelScope.launch (Dispatchers.Main) {
            var gson = Gson()
            val client = HttpClient (CIO){ //create and configure ktor client
                install(WebSockets)
                install(RSocketSupport)
            }
            val rSocket: RSocket = client.rSocket(path = "rsocket/api.v1.messages.stream", host = "10.0.2.2", port = 8080)

            val sendMessages: Flow<Payload> = flow{
                if(flag.value){
                    val sendMessage = SendMessage(contentSendMessage.value, conversationId.value, MessageType.TEXT)
                    //listMessage.value.plus(sendMessage)
                    val gsonSendMessage = gson.toJson(sendMessage)
                    emitOrClose(buildPayload { data("data: $gsonSendMessage ")})
                    flag.value = false
                }
            }


            rSocket.requestChannel(
                buildPayload {
                    this.metadata("message/x.rsocket.authentication.bearer.v0: Bearer $token")
                },
                sendMessages
            )
            /*stream.collect { payload: Payload ->
                val json = payload.data.readText()
                val gson = Gson()
                val receiverMessage = gson.fromJson(json, ReceiverMessage::class.java)
                listMessage.value.plus(receiverMessage)
            }*/
        }
    }
}
data class ReceiverMessage(
    val id: Long?,
    val content: String,
    val type: MessageType,
    val senderId: Long,
    val conversationId: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
)

enum class MessageType{
    TEXT,
    AUDIO,
    VIDEO,
    IMAGE
}

data class SendMessage(
    val content: String,
    val conversationId: Long,
    val type: MessageType
)