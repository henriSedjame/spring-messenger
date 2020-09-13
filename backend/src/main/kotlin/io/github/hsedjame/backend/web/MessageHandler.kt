package io.github.hsedjame.backend.web

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import model.Message
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.connectTcpAndAwait
import org.springframework.messaging.rsocket.dataWithType
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.ReplayProcessor

@Component("webfluxMessageHandler")
class MessageHandler(private val requesterBuilder: RSocketRequester.Builder) {

    private val processor = ReplayProcessor.create<Message>()
    private val sink = processor.sink()

    suspend fun send(serverRequest: ServerRequest) : ServerResponse {
        val message = serverRequest.awaitBody<Message>()
        sink.next(message)
        return ok().buildAndAwait()
    }

    @FlowPreview
    suspend fun stream(serverRequest: ServerRequest) : ServerResponse {
        val requester = requesterBuilder.dataMimeType(MediaType.APPLICATION_CBOR).connectTcpAndAwait("localhost", 9898)
        val replies = requester.route("bot.messages").dataWithType(processor).retrieveFlow<Message>()
        val broadcast = requester.route("bot.broadcast").retrieveFlow<Message>()
        val messages = processor.asFlow()
        return ok().sse().bodyAndAwait(flowOf(replies, messages, broadcast).flattenMerge())
    }
}
