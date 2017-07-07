package rest

import jooq.Tables.USERS
import jooq.tables.records.UsersRecord
import spark.Spark.get
import spark.Spark.post
import utils.db.sql.maxValue
import utils.db.sql.query
import utils.db.sqlite
import utils.rest.query.query
import utils.rest.toResponse
import utils.toJson

fun usersRest() {

    val path = "/users"

    get(path + "/login") { request, _ ->

        val login    = request.query("login").asString
        val password = request.query("password").asString

        sqlite.query {

            val user = select().from(USERS)
                    .where(USERS.LOGIN.eq(login))
                    .fetch()
                    ?.firstOrNull() as? UsersRecord ?: throw NotFoundError("user")

            if (password != user.password) throw QueryParamWrongError("password")

            user.toJson(
                    USERS.ID,
                    USERS.FNAME,
                    USERS.LNAME,
                    USERS.LOGIN,
                    USERS.PHOTO).toResponse()
        }

    }

    get(path + "/get") { request, _ ->

        val login    = request.query("login").asString

        sqlite.query {

            val user = select().from(USERS)
                    .where(USERS.LOGIN.eq(login))
                    .fetch()
                    ?.firstOrNull() as? UsersRecord ?: throw NotFoundError("user")

            user.toJson(
                    USERS.ID,
                    USERS.FNAME,
                    USERS.LNAME,
                    USERS.LOGIN,
                    USERS.PHOTO).toResponse()
        }

    }

    post(path + "/register") { request, _ ->

        val fName = request.query("fName").asString
        val lName = request.query("lName").asString
        val login = request.query("login").asString
        val password = request.query("password").asString
        val photo = request.query("photo").asString

        sqlite.query {

            val lastId = maxValue<Int>(USERS, USERS.ID) ?: -1

            val user = UsersRecord(
                    lastId + 1,
                    fName,
                    lName,
                    login,
                    password,
                    photo)

            insertInto(USERS)
                    .values(user)
                    .execute()

            user.toJson(
                    USERS.ID,
                    USERS.FNAME,
                    USERS.LNAME,
                    USERS.LOGIN,
                    USERS.PHOTO).toResponse()
        }

    }

}