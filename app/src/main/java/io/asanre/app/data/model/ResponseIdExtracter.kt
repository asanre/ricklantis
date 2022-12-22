package io.asanre.app.data.model

fun String.extractId() = split("/").last().toIntOrNull()