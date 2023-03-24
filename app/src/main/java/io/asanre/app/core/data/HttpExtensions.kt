package io.asanre.app.core.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*


suspend inline fun <reified T : Any, reified R> HttpClient.getResult(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> = runCatching { get(resource, builder).body() }

suspend inline fun <reified T : Any, reified R> HttpClient.postResult(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> = runCatching { post(resource, builder).body() }