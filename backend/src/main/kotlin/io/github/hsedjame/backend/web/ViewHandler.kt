package io.github.hsedjame.backend.web

import io.github.hsedjame.backend.repository.UserRepository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.renderAndAwait


class ViewHandler(private val userRepository: UserRepository) {

    suspend fun index(serverRequest: ServerRequest): ServerResponse {
        return ServerResponse.ok().renderAndAwait("index",
                mapOf("users" to userRepository.findAll())
        )
    }
}
