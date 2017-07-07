package utils.rest.query

import rest.QueryParamUndefinedError
import spark.Request

fun Request.query(name: String): QueryParam =
        queryOpt(name) ?: throw QueryParamUndefinedError(name)