package rest

import jooq.Tables.NEWS
import jooq.Tables.NEWS_COMMENTS
import jooq.tables.records.NewsCommentsRecord
import jooq.tables.records.NewsRecord
import spark.Spark.get
import spark.Spark.post
import utils.db.sql.maxValue
import utils.db.sql.query
import utils.db.sqlite
import utils.rest.query.query
import utils.rest.toResponse
import utils.toJson
import utils.toJsonArray

fun newsRest() {

    val path = "/news"

    get(path) { _, _ ->

        sqlite.query {
            select().from(NEWS)
                    .fetch()
                    .map {
                        it.toJson()
                    }
                    .toJsonArray()
                    .toResponse()
        }

    }

    post(path + "/post") { request, _ ->

        val title     = request.query("title").asString
        val content   = request.query("content").asString
        val image     = request.query("image").asString
        val userLogin = request.query("userLogin").asString

        sqlite.query {

            val lastId = maxValue<Int>(NEWS, NEWS.ID) ?: -1

            val new = NewsRecord(
                    lastId + 1,
                    title,
                    content,
                    image,
                    userLogin)

            insertInto(NEWS)
                    .values(new)
                    .execute()

            new.toJson().toResponse()
        }

    }

    get(path + "/comments") { request, _ ->

        val newId = request.query("newId").asInt

        sqlite.query {
            select().from(NEWS_COMMENTS)
                    .where(NEWS_COMMENTS.NEWID.eq(newId))
                    .fetch()
                    .map {
                        it.toJson()
                    }
                    .toJsonArray()
                    .toResponse()
        }

    }

    post(path + "/comments/post") { request, _ ->

        val newId     = request.query("newId").asInt
        val userLogin = request.query("userLogin").asString
        val content   = request.query("content").asString

        sqlite.query {

            val lastId = maxValue<Int>(NEWS_COMMENTS, NEWS.ID) ?: -1

            val comment = NewsCommentsRecord(
                    lastId + 1,
                    newId,
                    userLogin,
                    content)

            insertInto(NEWS_COMMENTS)
                    .values(comment)
                    .execute()

            comment.toJson().toResponse()
        }

    }

}