package io.asanre.app.core.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.gson.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

private const val BASE_URL = "https://rickandmortyapi.com/api/"

val coreDataModule = module {
    single {
        HttpClient(OkHttp) {
            expectSuccess = true
            engine {
                addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                })
            }

            defaultRequest {
                url(BASE_URL)
            }

            install(HttpCache)
            install(Resources)
            install(ContentNegotiation) {
                gson()
            }
        }
    }
}