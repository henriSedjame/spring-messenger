package io.github.hsedjame.bot

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import model.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class BotController {

    @FlowPreview
    @MessageMapping("bot.messages")
    fun messages(messages: Flow<Message>) : Flow<Message> = messages.flatMapMerge<Message, Message> {
        flow<Message> {
            when(it.content.toLowerCase().contains("hello")){
                true -> emit(Message(user="Mr bot", content = "Hey!! What can i do for you ?"))
            }
        }
    }

    @MessageMapping("bot.broadcast")
    fun broadcast() = flow<Message> {
        while (true) {
            emit(Message(user = "Mr bot", content = "Service message"))
            delay(500)
        }
    }
}
