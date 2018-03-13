package uk.co.catedroid.app.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.util.Log

import java.io.IOException
import java.net.HttpURLConnection

import javax.inject.Inject

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.R
import uk.co.catedroid.app.auth.LoginManager

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val state = MutableLiveData<LoginViewStates>()
    private val error = MutableLiveData<String>()

    @Inject
    lateinit var httpClient: OkHttpClient
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    enum class LoginViewStates {
        LOGIN_FORM,
        LOGIN_IN_PROGRESS,
        LOGGED_IN
    }

    init {
        (application as CATeApplication).netComponent.inject(this)
    }

    fun getState(): LiveData<LoginViewStates> {
        return state
    }

    fun getError(): LiveData<String> {
        return error
    }

    fun performLogin(username: String, password: String) {

        state.postValue(LoginViewStates.LOGIN_IN_PROGRESS)

        if (username == "" || password == "") {
            error.postValue("Please provide both a username and password")
            state.postValue(LoginViewStates.LOGIN_FORM)
            return
        }

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build()

        val credential = Credentials.basic(username, password)

        val call = httpClient.newCall(
                Request.Builder()
                        .url("https://cloud-vm-46-64.doc.ic.ac.uk:5000/auth")
                        .post(requestBody)
                        .header("Authorization", credential)
                        .build()
        )

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CATe", "LoginViewModel/IOException: " + e.message,
                        e.cause)
                error.postValue("An IO Exception has occurred")
                state.postValue(LoginViewStates.LOGIN_FORM)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                when {
                    response.code() == HttpURLConnection.HTTP_OK -> {
                        val loginManager = LoginManager(sharedPreferences)
                        loginManager.saveLogin(username, password, getApplication())
                        state.postValue(LoginViewStates.LOGGED_IN)
                    }
                    response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        error.postValue(getApplication<Application>().resources.getString(
                                R.string.error_invalid_credentials))
                        state.postValue(LoginViewStates.LOGIN_FORM)
                    }
                    response.code() == HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                        error.postValue(getApplication<Application>().resources.getString(
                                R.string.error_server_error))
                        val body = response.body()
                        if (body != null) {
                            Log.d("CATe", body.string())
                        } else {
                            Log.d("CATe", "LoginViewModel/onResponse Null response body")
                        }
                        state.postValue(LoginViewStates.LOGIN_FORM)
                    }
                    else -> {
                        Log.e("CATe", "LoginViewModel/onResponse something's gone wrong")
                        error.postValue("An unknown error has occurred")
                        state.postValue(LoginViewStates.LOGIN_FORM)
                    }
                }
            }
        })
    }
}
