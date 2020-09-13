package io.github.hsedjame.backend.web

import kotlinx.coroutines.FlowPreview
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@FlowPreview
fun routes(userHandler: UserHandler,
           messageHandler: MessageHandler,
           viewHandler: ViewHandler) = coRouter {

    GET("/", viewHandler::index)

    "user".nest {
        POST("/", userHandler::create)
        GET("/", userHandler::findAll)
        GET("/{login}", userHandler::findByLogin)
        DELETE("/{login}", userHandler::deleteByLogin)
    }

    "message".nest {
        POST("/send", messageHandler::send)
        GET("/stream", accept(MediaType.TEXT_EVENT_STREAM), messageHandler::stream)
    }

    resources("/**", ClassPathResource("static/"))
}
