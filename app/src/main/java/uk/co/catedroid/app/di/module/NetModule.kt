package uk.co.catedroid.app.di.module

import android.app.Application
import android.content.Context
import android.util.Log

import java.io.BufferedInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory

import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.catedroid.app.R
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.auth.LoginManager

@Module
class NetModule(private val app: Application) {

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        // Setting up SSL for cloud-vm-46-64
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        val trustManagers: Array<TrustManager>

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)

        val certInputStream = app.resources.openRawResource(R.raw.cloud_vm_46_64_cert)
        val bis = BufferedInputStream(certInputStream)
        val certificateFactory = CertificateFactory.getInstance("X.509")
        while (bis.available() > 0) {
            val cert = certificateFactory.generateCertificate(bis)
            keyStore.setCertificateEntry("cloud-vm-46-64.doc.ic.ac.uk", cert)
        }
        val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        trustManagers = trustManagerFactory.trustManagers
        sslContext.init(null, trustManagers, null)

        val authenticator = Authenticator { route, response ->
            val loginManager = LoginManager(app.getSharedPreferences(
                    "catedroid", Context.MODE_PRIVATE))
            if (loginManager.hasStoredCredentials()) {
                val credential = Credentials.basic(loginManager.login!!.username,
                        loginManager.login!!.password)
                return@Authenticator response.request().newBuilder()
                        .addHeader("Authorization", credential)
                        .build()
            }
            null
        }

        return OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory,
                        trustManagers[0] as X509TrustManager)
                .authenticator(authenticator)
                .build()
    }

    @Provides
    @Singleton
    fun providesCateService(client: OkHttpClient): CateService {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://cloud-vm-46-64.doc.ic.ac.uk:5000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(CateService::class.java)
    }
}
