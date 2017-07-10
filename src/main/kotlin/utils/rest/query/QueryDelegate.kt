package utils.rest.query

import spark.Request
import kotlin.reflect.KProperty

class QueryDelegate(
        private val request: Request) {

    abstract inner class Query<out T : Any> {
        protected abstract fun get(name: String): T
        private var value: T? = null
        operator fun getValue(thisRef: Any?, property: KProperty<*>) : T {
            if (value == null) {
                value = get(property.name)
            }
            return value!!
        }
    }

    inner class IntQuery : Query<Int>() { override fun get(name: String) = request.query(name).asInt }
    inner class StringQuery : Query<String>() { override fun get(name: String) = request.query(name).asString }
    inner class LongQuery : Query<Long>() { override fun get(name: String) = request.query(name).asLong }
    inner class FloatQuery : Query<Float>() { override fun get(name: String) = request.query(name).asFloat }
    inner class DoubleQuery : Query<Double>() { override fun get(name: String) = request.query(name).asDouble }
    inner class BooleanQuery : Query<Boolean>() { override fun get(name: String) = request.query(name).asBoolean }

    val int     get() = IntQuery()
    val string  get() = StringQuery()
    val long    get() = LongQuery()
    val float   get() = FloatQuery()
    val double  get() = DoubleQuery()
    val boolean get() = BooleanQuery()

}

val Request.query get() = QueryDelegate(this)