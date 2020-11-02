package io.github.hsedjame.backend

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import io.github.hsedjame.backend.repository.UserRepository
import io.github.hsedjame.backend.web.MessageHandler
import io.github.hsedjame.backend.web.UserHandler
import io.github.hsedjame.backend.web.ViewHandler
import io.github.hsedjame.backend.web.routes
import kotlinx.coroutines.FlowPreview
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbc
import org.springframework.fu.kofu.reactiveWebApplication
import org.springframework.fu.kofu.webflux.mustache
import org.springframework.fu.kofu.webflux.webFlux
import org.springframework.http.MediaType
import org.springframework.http.codec.cbor.Jackson2CborDecoder
import org.springframework.http.codec.cbor.Jackson2CborEncoder
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.messaging.rsocket.RSocketRequester
import kotlin.coroutines.coroutineContext


val dataConfig = configuration {
	beans {
		bean<UserRepository>()
	}

	listener<ApplicationReadyEvent> {
		ref<UserRepository>().init()
	}

	r2dbc {
		url = "r2dbc:postgresql://localhost:5432/spring-messenger"
		username = "postgres"
		password = "postgres"
	}

}

@FlowPreview
val webConfig = configuration {
	beans {
		bean(::routes)
		bean<UserHandler>()
		bean<MessageHandler>()
		bean<ViewHandler>()
	}
	webFlux {
		codecs {
			resource()
			string()

			jackson{
				indentOutput = true
			}
		}
		mustache()
	}
}

val rsocketConfig = configuration {
	beans {
		bean { RSocketRequester.builder().rsocketStrategies {

			val objectMapper: ObjectMapper = ref<Jackson2ObjectMapperBuilder>()
					.createXmlMapper(false)
					.factory(CBORFactory())
					.build()

			it.decoder(Jackson2CborDecoder(objectMapper, MediaType.APPLICATION_CBOR))
			it.encoder(Jackson2CborEncoder(objectMapper, MediaType.APPLICATION_CBOR))
		} }
	}
}

val app = reactiveWebApplication {
	enable(dataConfig)
	enable(webConfig)
	enable(rsocketConfig)
}

fun main(args: Array<String>) {
	app.run(args)
}
