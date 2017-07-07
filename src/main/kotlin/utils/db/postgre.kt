package utils.db

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.jooq.SQLDialect
import utils.db.base.Db
import utils.db.base.DbInfo
import java.sql.Connection
import javax.sql.DataSource

val postgre: Db by lazy {
    object : Db("postgre.properties") {

        override fun createDataSource(info: DbInfo): DataSource {

            val source = ComboPooledDataSource().apply {

                driverClass = info.driver
                jdbcUrl     = info.url
                user        = info.user
                password    = info.password

                initialPoolSize  = 3
                acquireIncrement = 5
                maxPoolSize      = 90
            }

            return source
        }

        override fun <R> connect(action: (Connection, SQLDialect) -> R): R {
            val connection = dataSource.connection
            val result = action(connection, SQLDialect.POSTGRES)
            connection.close()
            return result
        }

    }
}