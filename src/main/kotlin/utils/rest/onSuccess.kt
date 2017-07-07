package utils.rest

import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun onSuccess(body: JsonElement?): String {

    val json = JsonObject().apply {
        addProperty("code", 0)
        if (body != null) {
            add("response", body)
        }
    }

    return json.toString()
}