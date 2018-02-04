package uk.co.catedroid.app.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedInputStream;
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
import okhttp3.OkHttpClient;
import uk.co.catedroid.app.R;

@Module
public class NetModule {

    private Application app;

    public NetModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {

        // Setting up SSL for cloud-vm-46-64
        SSLContext sslContext;
        TrustManager[] trustManagers;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            InputStream certInputStream = app.getResources().openRawResource(R.raw.cloud_vm_46_64);
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

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(),
                        (X509TrustManager) trustManagers[0])
                .build();
    }
}
