package uk.co.catedroid.app.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.auth.LoginManager;

@Module
public class NetModule {

    private Application app;

    public NetModule(Application app) {
        this.app = app;
    }

    @Provides
    OkHttpClient providesOkHttpClient() {
        // Setting up SSL for cloud-vm-46-64
        SSLContext sslContext;
        TrustManager[] trustManagers;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            InputStream certInputStream = app.getResources().openRawResource(R.raw.cloud_vm_46_64_cert);
            BufferedInputStream bis = new BufferedInputStream(certInputStream);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = certificateFactory.generateCertificate(bis);
                keyStore.setCertificateEntry("cloud-vm-46-64.doc.ic.ac.uk", cert);
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            trustManagers = trustManagerFactory.getTrustManagers();
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
        } catch (Exception e) {
            Log.e("CATe", "NetModule/providesOkHttpClient " + e.getMessage(),
                    e.getCause());
            return null;
        }

        Authenticator authenticator = new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                LoginManager loginManager = new LoginManager(app.getSharedPreferences(
                        "catedroid", Context.MODE_PRIVATE));
                if (loginManager.hasStoredCredentials()) {
                    String credential = Credentials.basic(loginManager.getLogin().getUsername(), loginManager.getLogin().getPassword());
                    return response.request().newBuilder()
                            .addHeader("Authorization", credential)
                            .build();
                }
                return null;
            }
        };

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(),
                        (X509TrustManager) trustManagers[0])
                .authenticator(authenticator)
                .build();
    }

    @Provides
    @Singleton
    CateService providesCateService(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cloud-vm-46-64.doc.ic.ac.uk:5000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CateService.class);
    }
}
