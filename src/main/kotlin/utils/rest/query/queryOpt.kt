package utils.rest.query

import spark.Request

fun Request.queryOpt(name: String): QueryParam? {
    val param = queryParams(name)
    if (param != null) {
        return QueryParam(name, param)
    } else {
        return null
    }
}