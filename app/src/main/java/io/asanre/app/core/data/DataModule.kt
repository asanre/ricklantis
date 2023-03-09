package io.asanre.app.core.data

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.gson.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://rickandmortyapi.com/api/"

val coreDataModule = module {
    single {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .apply {
                    setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                }).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .client(client)
            .build()
    }

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