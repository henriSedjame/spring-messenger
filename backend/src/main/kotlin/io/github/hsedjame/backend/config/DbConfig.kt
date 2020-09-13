package io.github.hsedjame.backend.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.dialect.PostgresDialect

@Configuration
class DbConfig {

    @Bean
    fun connectionFactory() = PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration
                    .builder()
                    .host("localhost")
                    .database("spring-messenger")
                    .port(5432)
                    .username("postgres")
                    .password("postgres")
                    .schema("public")
                    .build()
    )

    @Bean
    fun r2dbcDatabaseClient(connectionFactory: PostgresqlConnectionFactory) = DatabaseClient.create(connectionFactory)

    @Bean
    fun reactiveDataAccessStrategy() = DefaultReactiveDataAccessStrategy(PostgresDialect())


}
