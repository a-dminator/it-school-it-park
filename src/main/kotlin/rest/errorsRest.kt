package rest

import spark.Request
import spark.Response
import spark.Spark
import spark.Spark.internalServerError
import utils.rest.onError

fun errorsRest() {

    exception<UndefinedError>           { e, _, _ -> onError(e) }
    exception<QueryParamUndefinedError> { e, _, _ -> onError(e) }
    exception<PostParamUndefinedError>  { e, _, _ -> onError(e) }

    Spark.exception(Exception::class.java) { e, _, response ->
        e.printStackTrace()
        response.body(onError(e as? Error ?: UndefinedError))
    }

    internalServerError { _, _ -> onError(UndefinedError) }
}

inline fun <reified T : Exception> exception(noinline handler: (T, Request, Response) -> String) {
    exception(T::class.java, handler)
}

@Suppress("UNCHECKED_CAST")
fun <T : Exception> exception(clazz: Class<out T>, handler: (T, Request, Response) -> String) {
    Spark.exception(clazz) { exception, request, response ->
        response.body(handler(exception as T, request, response))
    }
}

abstract class Error(val code: Int, val statusCode: Int, message: String) : Exception(message)

object UndefinedError                                              : Error(-1, 500, "undefined error")
class  QueryParamUndefinedError(paramName: String)                 : Error( 1, 500, "query param $paramName undefined")
class  PostParamUndefinedError(paramName: String)                  : Error( 2, 500, "post param $paramName undefined")
class  QueryParamWrongFormatError(paramName: String, type: String) : Error( 3, 400, "query param $paramName in not $type")
class  QueryParamWrongError(paramName: String)                     : Error( 4, 400, "query param $paramName incorrect")
class  NotFoundError(what: String)                                 : Error( 5, 404, "$what not found")
