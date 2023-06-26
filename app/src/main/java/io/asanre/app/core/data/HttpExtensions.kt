package io.asanre.app.core.data

import io.asanre.app.core.domain.extensions.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.HttpRequestBuilder


suspend inline fun <reified T : Any, reified R> HttpClient.getResult(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> = Try { get(resource, builder).body() }

suspend inline fun <reified T : Any, reified R> HttpClient.postResult(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): Result<R> = Try { post(resource, builder).body() }