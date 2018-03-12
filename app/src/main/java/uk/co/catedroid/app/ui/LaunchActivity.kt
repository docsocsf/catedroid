package uk.co.catedroid.app.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import javax.inject.Inject

import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.auth.LoginManager

class LaunchActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as CATeApplication).netComponent.inject(this)
        val loginManager = LoginManager(sharedPreferences)

        val intent: Intent

        intent = if (loginManager.hasStoredCredentials()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
