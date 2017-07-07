package utils.rest

import com.google.gson.JsonElement

fun JsonElement?.toResponse(): String = onSuccess(this)