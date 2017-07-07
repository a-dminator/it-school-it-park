package utils.rest.query

import rest.QueryParamWrongFormatError

class QueryParam(
        private val name: String,
        private val value: String) {

    val asInt get() = value.toIntOrNull() ?: throw QueryParamWrongFormatError(name, "Int")
    val asString get() = value
    val asLong get() = value.toLongOrNull() ?: throw QueryParamWrongFormatError(name, "Long")
    val asFloat: Float get() = value.toFloatOrNull() ?: throw QueryParamWrongFormatError(name, "Float")
    val asDouble: Double get() = value.toDoubleOrNull() ?: throw QueryParamWrongFormatError(name, "Double")
    val asBoolean get() = try { value.toBoolean() } catch (e: Throwable) { throw QueryParamWrongFormatError(name, "Boolean") }

}