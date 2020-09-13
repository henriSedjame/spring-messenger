package io.github.hsedjame.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BotApplication

fun main(args: Array<String>) {
	runApplication<BotApplication>(*args)
}
