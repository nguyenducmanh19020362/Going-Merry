package com.example.goingmerry.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goingmerry.repository.Retrofit
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.utils.io.core.*
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.core.WellKnownMimeType
import io.rsocket.kotlin.emitOrClose
import io.rsocket.kotlin.ktor.client.RSocketSupport
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.compositeMetadata
import io.rsocket.kotlin.metadata.security.BearerAuthMetadata
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.PayloadMimeType
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URL
import java.time.Instant

class AnonymousChatViewModel: ViewModel() {
    var stateAnonymousChat = mutableStateOf(false)

    private val _listIncognitoMessageMessages = MutableStateFlow(listOf<IncognitoMessage>())
    val listIncognitoMessage = _listIncognitoMessageMessages.asStateFlow()

    var contentSendMessage = mutableStateOf("")
    var flag = mutableStateOf(false)

    var jobReceiver: Job? = null
    var jobSend: Job? = null

    fun setStateAnonymousChat(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            val authService = Retrofit.getAuthService()
            val responseService = authService.controlChat("Bearer $token")
            if(responseService.isSuccessful){
                responseService.body()?.let {
                    stateAnonymousChat.value = it.dataControlAnonymousChat
                    Log.e("Logging", "Response TokenDto: $it.")
                }
            }else{
                Log.e("controlAnonymous","false")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun receiverMessages(loginViewModel: LoginViewModel){
        val bearerAuthMetadata = BearerAuthMetadata(loginViewModel.token.value)
        val routeMetadata = RoutingMetadata("api.v1.incognito.stream")
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
            val rSocket: RSocket = client.rSocket(path = "/rsocket", host = com.example.goingmerry.URL.host, port = 8080)

            val stream: Flow<Payload> = rSocket.requestStream(
                buildPayload {
                    compositeMetadata {
                        add(bearerAuthMetadata)
                        add(routeMetadata)
                    }
                    data(Instant.MIN.epochSecond.toString())
                }
            )
            stream.collect { payload: Payload ->
                val json = payload.data.readText()
                val incognitoMessageMessage = gson.fromJson(json, IncognitoMessage::class.java)
                _listIncognitoMessageMessages.tryEmit(listIncognitoMessage.value + incognitoMessageMessage)
            }
        }
    }

    fun sendMessages(loginViewModel: LoginViewModel){
        val bearerAuthMetadata = BearerAuthMetadata(loginViewModel.token.value)
        val routeMetadata = RoutingMetadata("api.v1.incognito.stream")
        jobSend = viewModelScope.launch (Dispatchers.IO) {
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
            val rSocket: RSocket = client.rSocket(path = "/rsocket", host = com.example.goingmerry.URL.host, port = 8080)

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
                            val sendMessage = SendIncognitoMessage(contentSendMessage.value, IncognitoMessageType.TEXT)
                            val gsonSendMessage = gson.toJson(sendMessage)
                            emitOrClose(
                                buildPayload {
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

    fun getContentSendMessage(): String{
        return contentSendMessage.value
    }

    fun setContentSendMessage(content: String){
        contentSendMessage.value = content
    }

    fun setFlag(_flag: Boolean){
        flag.value = _flag
    }

    fun getFlag(): Boolean{
        return flag.value
    }
}

enum class IncognitoMessageType{
    TEXT
}
data class IncognitoMessage(
    val id: Long,
    val content: String,
    val type: IncognitoMessageType,
    val sentAt: String,
    val senderId: Long
)

data class SendIncognitoMessage(
    val content: String,
    val type: IncognitoMessageType
)