
import rest.errorsRest
import rest.newsRest
import rest.usersRest
import spark.Spark
import spark.servlet.SparkApplication

// -Djava.awt.headless=true -server -Xms1m -Xmx16M -XX:MaxPermSize=64k -XX:+UseCompressedOops
// -Djava.awt.headless=true -server -Xms32m -Xmx256M -XX:MaxPermSize=16m -XX:+UseCompressedOops

class Application : SparkApplication {

    override fun init() {

        Spark.staticFiles.location("/public")
        Spark.redirect.get("index", "index.html")

        usersRest()
        newsRest()
        errorsRest()

//        usersRest()
//        driversRest()
//        chatRest()
//        subscriptionsRest()
//        paysRest()
//        tariffsRest()
//        testRest()
//        errorsRest()
    }

}