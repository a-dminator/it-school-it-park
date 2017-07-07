package utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jooq.Field
import org.jooq.Record

private val gson = Gson()

fun Record.toJson(vararg fields: Field<*>): JsonObject {
    val json = gson.toJsonTree(intoMap()).asJsonObject
    if (fields.isNotEmpty()) {
        val fieldNames = fields.map { it.name }
        json.entrySet().toList().forEach { (key, _) ->
            if (key !in fieldNames) {
                json.remove(key)
            }
        }
    }
    return json
}

fun Iterable<JsonObject>.toJsonArray() =
        JsonArray().apply {
            this@toJsonArray.forEach { add(it) }
        }