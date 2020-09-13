package io.github.hsedjame.backend.web

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouteConfiguration {

    @FlowPreview
    @Bean
    fun routes(userHandler: UserHandler,
               messageHandler: MessageHandler,
               viewHandler: ViewHandler) = coRouter {

        GET("/index.html", viewHandler::index)

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
    }
}
