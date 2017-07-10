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

        val login    by request.query.string
        val password by request.query.string

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

        val login by request.query.string

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

        val fName    by request.query.string
        val lName    by request.query.string
        val login    by request.query.string
        val password by request.query.string
        val photo    by request.query.string

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
                    .set(user)
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