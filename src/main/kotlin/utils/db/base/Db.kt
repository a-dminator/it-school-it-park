package utils.db.base
import org.jooq.SQLDialect
import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.util.*
import javax.sql.DataSource

abstract class Db(
        private val propertiesPath: String) {

    private val info by lazy {

        val props =
                loadPropertiesFromLocalDir(propertiesPath) ?:
                loadPropertiesFromResources(propertiesPath, javaClass)

        DbInfo(
                props.getProperty("DRIVER"),
                props.getProperty("URL"),
                props.getProperty("USER"),
                props.getProperty("PASSWORD")).apply {
            Class.forName(driver)
        }
    }

    val dataSource: DataSource by lazy {
        createDataSource(info)
    }

    protected abstract fun createDataSource(info: DbInfo): DataSource
    abstract fun <R> connect(action: (Connection, SQLDialect) -> R): R

    private fun loadPropertiesFromLocalDir(path: String): Properties? {

        try {

            val stream = FileInputStream(File("./", path))
            val props = Properties().apply {
                load(stream)
            }

            stream.close()

            return props

        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }

    }

    private fun loadPropertiesFromResources(path: String, clazz: Class<*>): Properties {
        val stream = clazz.classLoader.getResourceAsStream(path)
        val props = Properties().apply {
            load(stream)
        }
        stream.close()
        return props
    }

}