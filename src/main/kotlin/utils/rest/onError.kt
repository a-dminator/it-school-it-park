package utils.rest

import com.google.gson.JsonObject
import rest.Error

fun onError(error: Error): String {

    val json = JsonObject().apply {
        addProperty("code", error.code)
        addProperty("message", error.message)
    }

    return json.toString()
}