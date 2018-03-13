package uk.co.catedroid.app.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import uk.co.catedroid.app.R
import uk.co.catedroid.app.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.login_layout_login_form)
    lateinit var layoutLoginForm: View
    @BindView(R.id.login_layout_in_progress)
    lateinit var layoutInProgress: View

    @BindView(R.id.login_username_field)
    lateinit var usernameField: EditText
    @BindView(R.id.login_password_field)
    lateinit var passwordField: EditText
    @BindView(R.id.login_error_field)
    lateinit var errorField: TextView
    @BindView(R.id.login_login_button)
    lateinit var loginButton: Button

    @BindView(R.id.login_app_info)
    lateinit var appInfoField: TextView

    private var model: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        this.model = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        this.model!!.getError().observe(this, Observer { s -> setErrorText(s) })

        this.model!!.getState().observe(this, Observer { loginViewStates ->
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

        this.appInfoField.text = resources.getString(R.string.app_info_format,
                getString(R.string.app_name), versionText)
    }

    @OnClick(R.id.login_login_button)
    fun doLogin() {
        val username = usernameField.text.toString()
        val password = passwordField.text.toString()
        model!!.performLogin(username, password)
    }

    private fun setErrorText(text: String?) {
        if (text != null && text != "") {
            errorField.text = text
            errorField.visibility = View.VISIBLE
        } else {
            errorField.text = ""
            errorField.visibility = View.GONE
        }
    }

    private fun showLoginForm() {
        layoutLoginForm.visibility = View.VISIBLE
        layoutInProgress.visibility = View.GONE
    }

    private fun showLoginInProgress() {
        layoutLoginForm.visibility = View.GONE
        layoutInProgress.visibility = View.VISIBLE
    }

    private fun goToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
