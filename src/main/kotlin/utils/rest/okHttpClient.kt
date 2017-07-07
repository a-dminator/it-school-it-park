package utils.rest

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.net.ssl.SSLContext.getInstance
import javax.net.ssl.X509TrustManager

val okHttpClient = buildUnsafeOkHttpClient().newBuilder()
        .connectTimeout(Int.MAX_VALUE.toLong(), MILLISECONDS)
        .readTimeout(Int.MAX_VALUE.toLong(), MILLISECONDS)
        .writeTimeout(Int.MAX_VALUE.toLong(), MILLISECONDS)
        .build()!!

private fun buildUnsafeOkHttpClient(): OkHttpClient {

    try {

        val trustAllCerts =
                arrayOf<X509TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class) override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) { }
                            @Throws(CertificateException::class) override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) { }
                            override fun getAcceptedIssuers(): Array<X509Certificate>? { return emptyArray() }
                        }
                )

        return OkHttpClient.Builder()
                .sslSocketFactory(
                        getInstance("SSL").apply {
                            init(null, trustAllCerts, SecureRandom())
                        }.socketFactory,
                        trustAllCerts[0])
                .hostnameVerifier { _, _ -> true }
                .build()

    } catch (e: Throwable) {
        throw RuntimeException(e)
    }

}
