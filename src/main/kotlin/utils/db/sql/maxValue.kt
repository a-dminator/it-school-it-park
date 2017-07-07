package utils.db.sql

import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Table

fun <T> DSLContext.maxValue(table: Table<*>, field: Field<T>) =
        select(field)
                .from(table)
                .max()
                ?.value1()