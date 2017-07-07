package utils.db.sql

import org.jooq.DSLContext
import org.jooq.impl.DSL
import utils.db.base.Db

fun <T> Db.query(query: DSLContext.() -> T): T {

    try {

        return connect { connection, dialect ->
            val context = DSL.using(connection, dialect)
            val result = context.query()
            context.close()
            result
        }

    } catch (e: Throwable) {
        throw e
    }

}