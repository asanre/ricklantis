package io.asanre.app.core.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.gson.*
import org.koin.dsl.module

private const val BASE_URL = "https://rickandmortyapi.com/api/"

val coreDataModule = module {
    single {
        HttpClient(OkHttp) {
            install(Resources)

            defaultRequest {
                url(BASE_URL)
            }

            install(Logging) {
                level = LogLevel.BODY
            }

            install(ContentNegotiation) {
                gson()
            }
        }
    }
}