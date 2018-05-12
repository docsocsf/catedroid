package uk.co.catedroid.app.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_login.*

import uk.co.catedroid.app.R
import uk.co.catedroid.app.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        this.viewModel.getError().observe(this, Observer { s -> setErrorText(s) })

        this.viewModel.getState().observe(this, Observer { loginViewStates ->
            if (loginViewStates != null) {
                when (loginViewStates) {
                    LoginViewModel.LoginViewStates.LOGIN_FORM -> showLoginForm()
                    LoginViewModel.LoginViewStates.LOGIN_IN_PROGRESS -> showLoginInProgress()
                    LoginViewModel.LoginViewStates.LOGGED_IN -> goToMainActivity()
                }
            } else {
                Log.e("CATe", "LoginActivity observed a null state")
            }
        })

        // Setting app info text
        var versionText = ""
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            versionText = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("CATe", "LoginActivity: Can't get package info for package ["
                    + packageName + "]")
        }



        login_app_info.text = resources.getString(R.string.app_info_format,
                getString(R.string.app_name), versionText)

        login_login_button.setOnClickListener {
            val username = login_username_field.text.toString()
            val password = login_password_field.text.toString()
            viewModel.performLogin(username, password)
        }
    }

    private fun setErrorText(text: String?) {
        if (text != null && text != "") {
            login_error_field.text = text
            login_error_field.visibility = View.VISIBLE
        } else {
            login_error_field.text = ""
            login_error_field.visibility = View.GONE
        }
    }

    private fun showLoginForm() {
        login_layout_login_form.visibility = View.VISIBLE
        login_layout_in_progress.visibility = View.GONE
    }

    private fun showLoginInProgress() {
        login_layout_login_form.visibility = View.GONE
        login_layout_in_progress.visibility = View.VISIBLE
    }

    private fun goToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
