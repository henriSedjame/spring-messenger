package io.github.hsedjame.backend.web

import io.github.hsedjame.backend.model.User
import io.github.hsedjame.backend.repository.UserRepository
import org.springframework.http.HttpStatus

import org.springframework.web.reactive.function.server.*


class UserHandler(private val userRepository: UserRepository) {

    suspend fun findAll(serverRequest: ServerRequest) = ServerResponse.ok().bodyAndAwait(userRepository.findAll())

    suspend fun findByLogin(serverRequest: ServerRequest) = userRepository.findByLogin(serverRequest.pathVariable("login"))
            ?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?: ServerResponse.notFound().buildAndAwait()

    suspend fun deleteByLogin(serverRequest: ServerRequest) = userRepository.findByLogin(serverRequest.pathVariable("login"))
            .let { ServerResponse.ok().buildAndAwait() }

    suspend fun create(serverRequest: ServerRequest) = userRepository.create(serverRequest.awaitBody<User>())
            .let { ServerResponse.status(HttpStatus.CREATED).buildAndAwait() }
}
