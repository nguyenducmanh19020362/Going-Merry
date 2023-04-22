package com.example.goingmerry.viewModel

import AccountQuery
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.utils.io.core.*
import io.netty.buffer.ByteBufAllocator
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.core.WellKnownMimeType
import io.rsocket.kotlin.emitOrClose
import io.rsocket.kotlin.ktor.client.RSocketSupport
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.compositeMetadata
import io.rsocket.kotlin.metadata.security.BearerAuthMetadata
import io.rsocket.kotlin.payload.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import type.MessageState
import java.time.Instant
import java.util.List


class ChatBoxViewModel: ViewModel() {
    var stateSockets = mutableStateOf("OFF")
    var conversationId = mutableStateOf(0L)
    private val _listReceiverMessage = MutableStateFlow(listOf<DirectMessage>())
    val listReceiverMessage = _listReceiverMessage.asStateFlow()
    var contentSendMessage = mutableStateOf("")
    var flag = mutableStateOf(false)
    var jobReceiver: Job? = null
    var sendJob: Job? = null
    @RequiresApi(Build.VERSION_CODES.O)
    fun receiverMessages(loginViewModel: LoginViewModel, homeViewModel: HomeViewModel){
        stateSockets.value = "ON"
        val bearerAuthMetadata = BearerAuthMetadata(loginViewModel.token.value)
        val routeMetadata = RoutingMetadata("api.v1.messages.stream")
        jobReceiver?.cancel()
        jobReceiver = viewModelScope.launch (Dispatchers.IO){
            val gson = Gson()
            val client = HttpClient (CIO){ //create and configure ktor client
                install(WebSockets)
                install(RSocketSupport){
                    connector {
                        connectionConfig {
                            payloadMimeType = PayloadMimeType(
                                data = WellKnownMimeType.ApplicationJson,
                                metadata = WellKnownMimeType.MessageRSocketCompositeMetadata
                            )
                        }
                    }
                }
            }
            val rSocket: RSocket = client.rSocket(path = "/rsocket", host = "192.168.57.104", port = 8080)

            val stream: Flow<Payload> = rSocket.requestStream(
                buildPayload {
                    compositeMetadata {
                        add(bearerAuthMetadata)
                        add(routeMetadata)
                    }
                    Log.e("time", Instant.now().toString())
                    data(Instant.now().epochSecond.toString())
                }
            )
            stream.collect { payload: Payload ->
                val json = payload.data.readText()
                val receiverMessage = gson.fromJson(json, ReceiverMessage::class.java)
                Log.e("hi", receiverMessage.toString())
                for(item in homeViewModel.conversations.value){
                    if(item.id.toLong() == receiverMessage.conversationId){
                        var name = ""
                        for(member in item.members){
                            if(member.id.toLong() == receiverMessage.senderId){
                                name = member.name
                            }
                        }
                        val directMessage = DirectMessage(
                            receiverMessage.conversationId.toString(),
                            receiverMessage.senderId.toString(),
                            receiverMessage.content,
                            name,
                            Instant.parse(receiverMessage.sentAt).epochSecond.toInt(),
                        )
                        _listReceiverMessage.emit(listReceiverMessage.value + directMessage)
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
    fun sendMessages(loginViewModel: LoginViewModel){
        Log.e("send", "sendMessage")
        stateSockets.value = "ON"
        val bearerAuthMetadata = BearerAuthMetadata(loginViewModel.token.value)
        val routeMetadata = RoutingMetadata("api.v1.messages.stream")
        sendJob?.cancel()
        sendJob = viewModelScope.launch (Dispatchers.IO) {
            val gson = Gson()
            val client = HttpClient (CIO){ //create and configure ktor client
                install(WebSockets)
                install(RSocketSupport){
                    connector {
                        connectionConfig {
                            payloadMimeType = PayloadMimeType(
                                data = WellKnownMimeType.ApplicationJson,
                                metadata = WellKnownMimeType.MessageRSocketCompositeMetadata
                            )
                        }
                    }
                }
            }
            val rSocket: RSocket = client.rSocket(path = "/rsocket", host = "192.168.57.104", port = 8080)

            rSocket.requestChannel(
                buildPayload {
                    compositeMetadata {
                        add(bearerAuthMetadata)
                        add(routeMetadata)
                    }
                    data(ByteReadPacket.Empty)
                },
                flow{
                    while (true){
                        if(flag.value){
                            val sendMessage = SendMessage(contentSendMessage.value, conversationId.value, MessageType.TEXT)
                            //listMessage.value.plus(sendMessage)
                            val gsonSendMessage = gson.toJson(sendMessage)
                            emitOrClose(
                                buildPayload {
                                    Log.e("gsonMessage", gsonSendMessage)
                                    compositeMetadata {
                                        add(bearerAuthMetadata)
                                        add(routeMetadata)
                                    }
                                    data(gsonSendMessage)
                                }
                            )
                            flag.value = false
                        }
                    }

                }
            ).collect()
        }
    }
}
data class ReceiverMessage(
    val id: Long?,
    val content: String,
    val type: MessageType,
    val senderId: Long,
    val conversationId: Long,
    val sentAt: String,
)
data class DirectMessage(
    val idConversation: String,
    val idSender: String,
    val messageContent: String,
    val messageName: String,
    val sendAt: Int
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
    val type: MessageType,
)