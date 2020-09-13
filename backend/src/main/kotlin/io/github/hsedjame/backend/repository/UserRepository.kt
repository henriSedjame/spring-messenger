package io.github.hsedjame.backend.repository

import io.github.hsedjame.backend.model.User
import io.r2dbc.postgresql.client.Client
import io.r2dbc.spi.Row
import kotlinx.coroutines.runBlocking
import org.springframework.data.r2dbc.convert.EntityRowMapper
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.*
import org.springframework.transaction.reactive.TransactionalOperator

const val initSql = "CREATE TABLE IF NOT EXISTS users( id BIGINT PRIMARY KEY GENERATED ALWAYS AS  IDENTITY NOT NULL , login VARCHAR , firstname VARCHAR , lastname VARCHAR)"

class UserRepository(private val client: DatabaseClient) {

    suspend fun create(user: User) = client
            .sql("INSERT INTO users (login, firstname, lastname) VALUES ('${user.login}', '${user.firstname}', '${user.lastname}');")
            .await()

    fun findAll() = client
            .sql("SELECT * FROM users")
            .map(userMapper)
            .flow()

    suspend fun findByLogin(login: String): User? = client
            .sql("SELECT * FROM users WHERE login=:login")
            .bind("login", login)
            .map<User>(userMapper)
            .awaitFirstOrNull()

    suspend fun deleteByLogin(login: String) = client
            .sql("DELETE FROM WHERE login=:login")
            .await()

    fun init() = runBlocking {
        client.sql(initSql).fetch().awaitRowsUpdated()
        create(User(null).apply {
            login = "clenain"
            firstname = "Chloe"
            lastname = "LENAIN"
        })
        create(User(null).apply {
            login = "hsedjame"
            firstname = "Henri "
            lastname = "SEDJAME"
        })
    }

}

val userMapper: (Row) -> User = { row ->
    User(null)
            .apply {
                this.login = row.get("login", String::class.java)
                this.lastname = row.get("lastname", String::class.java)
                this.firstname = row.get("firstname", String::class.java)
            }
}
