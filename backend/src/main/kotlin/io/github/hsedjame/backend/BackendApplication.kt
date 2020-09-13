package io.github.hsedjame.backend

import io.github.hsedjame.backend.model.User
import io.github.hsedjame.backend.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitRowsUpdated
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

const val sql = "CREATE TABLE IF NOT EXISTS users( id BIGINT PRIMARY KEY GENERATED ALWAYS AS  IDENTITY NOT NULL , login VARCHAR , firstname VARCHAR , lastname VARCHAR)"

@SpringBootApplication
class BackendApplication {

	@Bean
	fun run(operator: TransactionalOperator,
			client: DatabaseClient,
			userRepository: UserRepository)  = CommandLineRunner {

		args ->  runBlocking {
			operator.executeAndAwait {
				client.execute(sql).fetch().awaitRowsUpdated()

				userRepository.deleteAll()

				userRepository.save(User(id = null).apply {
					login = "clenain"
					firstname = "chloe"
					lastname = "LE NAIN"
				})
				userRepository.save(User(id = null).apply {
					login = "llenain"
					firstname = "lea"
					lastname = "LE NAIN"
				})

			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
