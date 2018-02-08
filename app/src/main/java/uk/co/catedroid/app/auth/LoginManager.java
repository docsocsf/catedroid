package uk.co.catedroid.app.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

public class LoginManager {

    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "catedroid.auth.key";
    private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";
    private static final String USERNAME_KEY = "catedroid.auth.current.username";
    private static final String PASSWORD_KEY = "catedroid.auth.current.password";

    private SharedPreferences sp;

    public LoginManager(SharedPreferences sharedPreferences) {
        this.sp = sharedPreferences;
    }

    public Credentials getLogin() {
        String username = sp.getString(USERNAME_KEY, "");
        String passwordString = sp.getString(PASSWORD_KEY, "");
        byte[] passwordEncrypted = Base64.decode(passwordString, Base64.DEFAULT);

        String password;

        try {
            password = new String(decrypt(passwordEncrypted), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("CATe", "LoginManager/saveLogin: UnsupportedEncoding", e.getCause());
            return null;
        }

        if (username.equals("") || password.equals("")) {
            return null;
        } else {
            return new Credentials(username, password);
        }
    }

    public boolean saveLogin(String username, String password, Context context) {
        SharedPreferences.Editor editor = sp.edit();
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("CATe", "LoginManager/saveLogin: UnsupportedEncoding", e.getCause());
            return false;
        }

        Log.d("CATe", "Encrypting password");
        byte[] passwordEncrypted = encrypt(context, passwordBytes);
        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSWORD_KEY, Base64.encodeToString(passwordEncrypted, Base64.DEFAULT));
        editor.apply();
        return true;
    }

    public void logout() {
        SharedPreferences.Editor e = sp.edit();
        e.remove(USERNAME_KEY);
        e.remove(PASSWORD_KEY);
        e.apply();
    }

    public boolean hasStoredCredentials() {
        return !(sp.getString(USERNAME_KEY, "").equals("") ||
                sp.getString(PASSWORD_KEY, "").equals("") );
    }

    private byte[] encrypt(Context context, byte[] secret) {
        try {
            Log.d("CATe", "Loading keystore...");
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Log.d("CATe", "KeyStore does not contain encryption key - creating");

                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEYSTORE);
                generator.initialize(spec);
                generator.generateKeyPair();
            }

            // Get private key
            Log.d("CATe", "Retrieving private key");
            KeyStore.PrivateKeyEntry privateKey = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

            // Encrypt text
            Log.d("CATe", "Creating cipher instance");
            Cipher inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
            inputCipher.init(Cipher.ENCRYPT_MODE, privateKey.getCertificate().getPublicKey());

            Log.d("CATe", "Encrypting password byte array");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
            cipherOutputStream.write(secret);
            cipherOutputStream.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            Log.e("CATe", "Exception whilst encrypting: " + e.getMessage(), e.getCause());
            return null;
        }
    }

    private byte[] decrypt(byte[] encrypted) {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Log.w("CATe", "KeyStore does not contain decryption key");
                return null;
            }

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

            Cipher output = Cipher.getInstance(RSA_MODE);
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(encrypted), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];

            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i);
            }

            return bytes;
        } catch (Exception e) {
            Log.e("CATe", "Exception whilst decrypting: " + e.getMessage(), e.getCause());
            return null;
        }
    }
}
