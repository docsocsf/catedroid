package uk.co.catedroid.app.auth

import android.content.Context
import android.content.SharedPreferences
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import android.util.Log

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.ArrayList
import java.util.Calendar

import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal



class LoginManager(private val sp: SharedPreferences) {

    val login: Credentials?
        get() {
            val username = sp.getString(USERNAME_KEY, "")
            val passwordString = sp.getString(PASSWORD_KEY, "")
            val passwordEncrypted = Base64.decode(passwordString, Base64.DEFAULT)

            val password: String

            try {
                password = String(decrypt(passwordEncrypted)!!)
            } catch (e: UnsupportedEncodingException) {
                Log.wtf("CATe", "LoginManager/saveLogin: UnsupportedEncoding", e.cause)
                return null
            }

            return if (username == "" || password == "") {
                null
            } else {
                Credentials(username!!, password)
            }
        }

    fun saveLogin(username: String, password: String, context: Context): Boolean {
        val editor = sp.edit()
        val passwordBytes: ByteArray
        try {
            passwordBytes = password.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            Log.wtf("CATe", "LoginManager/saveLogin: UnsupportedEncoding", e.cause)
            return false
        }

        Log.d("CATe", "Encrypting password")
        val passwordEncrypted = encrypt(context, passwordBytes)
        editor.putString(USERNAME_KEY, username)
        editor.putString(PASSWORD_KEY, Base64.encodeToString(passwordEncrypted, Base64.DEFAULT))
        editor.apply()
        return true
    }

    fun logout() {
        val e = sp.edit()
        e.remove(USERNAME_KEY)
        e.remove(PASSWORD_KEY)
        e.apply()
    }

    fun hasStoredCredentials(): Boolean {
        return !(sp.getString(USERNAME_KEY, "") == "" || sp.getString(PASSWORD_KEY, "") == "")
    }

    private fun encrypt(context: Context, secret: ByteArray): ByteArray? {
        try {
            Log.d("CATe", "Loading keystore...")
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Log.d("CATe", "KeyStore does not contain encryption key - creating")

                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 30)

                val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(KEY_ALIAS)
                    .setSubject(X500Principal("CN=$KEY_ALIAS"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()

                val generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEYSTORE)
                generator.initialize(spec)
                generator.generateKeyPair()
            }

            // Get private key
            Log.d("CATe", "Retrieving private key")
            val privateKey = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry

            // Encrypt text
            Log.d("CATe", "Creating cipher instance")
            val inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
            inputCipher.init(Cipher.ENCRYPT_MODE, privateKey.certificate.publicKey)

            Log.d("CATe", "Encrypting password byte array")
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
            cipherOutputStream.write(secret)
            cipherOutputStream.close()

            return outputStream.toByteArray()
        } catch (e: Exception) {
            Log.e("CATe", "Exception whilst encrypting: " + e.message, e.cause)
            return null
        }

    }

    private fun decrypt(encrypted: ByteArray): ByteArray? {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Log.w("CATe", "KeyStore does not contain decryption key")
                return null
            }

            val privateKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry

            val output = Cipher.getInstance(RSA_MODE)
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
            val cipherInputStream = CipherInputStream(ByteArrayInputStream(encrypted), output)

            val values = ArrayList<Byte>()
            for (b in cipherInputStream.readBytes()) {
                values.add(b)
            }

            val bytes = ByteArray(values.size)

            for (i in bytes.indices) {
                bytes[i] = values[i]
            }

            return bytes
        } catch (e: Exception) {
            Log.e("CATe", "Exception whilst decrypting: " + e.message, e.cause)
            return null
        }

    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "catedroid.auth.key"
        private const val RSA_MODE = "RSA/ECB/PKCS1Padding"
        private const val USERNAME_KEY = "catedroid.auth.current.username"
        private const val PASSWORD_KEY = "catedroid.auth.current.password"
    }
}
