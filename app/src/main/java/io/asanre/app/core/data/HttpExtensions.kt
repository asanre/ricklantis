package io.asanre.app.core.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*


suspend inline fun <reified T : Any, reified R> HttpClient.getBody(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): R = get(resource, builder).body()