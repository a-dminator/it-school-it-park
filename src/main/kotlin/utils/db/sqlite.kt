package utils.db

import com.mchange.v2.c3p0.DataSources
import org.jooq.SQLDialect
import org.sqlite.SQLiteConfig
import org.sqlite.SQLiteDataSource
import utils.db.base.Db
import utils.db.base.DbInfo
import java.sql.Connection
import javax.sql.DataSource

val sqlite: Db by lazy {
    object : Db("sqlite.properties") {

        override fun createDataSource(info: DbInfo): DataSource {

            val config = SQLiteConfig().apply {
                setReadOnly(false)
                setPageSize(4096) // in bytes
                setCacheSize(2000) // number of pages
                setSynchronous(SQLiteConfig.SynchronousMode.OFF)
                setJournalMode(SQLiteConfig.JournalMode.OFF)
            }

            val unpooledSource = SQLiteDataSource(config).apply {
                url = info.url
            }

            return DataSources.pooledDataSource(unpooledSource)
        }

        @Synchronized override fun <R> connect(action: (Connection, SQLDialect) -> R): R {
            val connection = dataSource.connection
            val result = action(connection, SQLDialect.SQLITE)
            connection.close()
            return result
        }

    }
}