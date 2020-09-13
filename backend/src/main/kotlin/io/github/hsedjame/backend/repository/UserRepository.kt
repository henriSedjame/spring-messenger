package io.github.hsedjame.backend.repository

import io.github.hsedjame.backend.model.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {

    @Query("SELECT * FROM users WHERE login=:login")
    suspend fun findByLogin(login: String): User?

    @Query("DELETE FROM users WHERE login=:login")
    suspend fun deleteByLogin(login: String)

}
