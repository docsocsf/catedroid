package uk.co.catedroid.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.auth.LoginManager;

public class LoginViewModel extends AndroidViewModel {

    public enum LOGIN_VIEW_STATES {
        LOGIN_FORM,
        LOGIN_IN_PROGRESS,
        LOGGED_IN
    }

    private final MutableLiveData<LOGIN_VIEW_STATES> state = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    OkHttpClient httpClient;

    @Inject
    SharedPreferences sharedPreferences;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        ((CATeApplication) application).getNetComponent().inject(this);
    }

    public LiveData<LOGIN_VIEW_STATES> getState() {
        return state;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void performLogin(String username, String password) {
        final String finalUsername = username;
        final String finalPassword = password;

        state.postValue(LOGIN_VIEW_STATES.LOGIN_IN_PROGRESS);

        if (username.equals("") || password.equals("")) {
            error.postValue("Please provide both a username and password");
            state.postValue(LOGIN_VIEW_STATES.LOGIN_FORM);
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();

        Call call = httpClient.newCall(
                new Request.Builder()
                        .url("https://cloud-vm-46-64.doc.ic.ac.uk:5000/auth")
                        .post(requestBody)
                        .build()
        );

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,@NonNull IOException e) {
                Log.e("CATe", "LoginViewModel/IOException: " + e.getMessage(), e.getCause());
                error.postValue("An IO Exception has occurred");
                state.postValue(LOGIN_VIEW_STATES.LOGIN_FORM);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LoginManager loginManager = new LoginManager(sharedPreferences);
                    loginManager.saveLogin(finalUsername, finalPassword, getApplication());
                    state.postValue(LOGIN_VIEW_STATES.LOGGED_IN);
                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    error.postValue(getApplication().getResources().getString(
                            R.string.error_invalid_credentials));
                    state.postValue(LOGIN_VIEW_STATES.LOGIN_FORM);
                } else if (response.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    error.postValue(getApplication().getResources().getString(
                            R.string.error_server_error));
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.d("CATe", body.string());
                    } else {
                        Log.d("CATe", "LoginViewModel/onResponse Null response body");
                    }
                    state.postValue(LOGIN_VIEW_STATES.LOGIN_FORM);
                } else {
                    Log.e("CATe", "LoginViewModel/onResponse something's gone wrong");
                    error.postValue("An unknown error has occurred");
                    state.postValue(LOGIN_VIEW_STATES.LOGIN_FORM);
                }
            }
        });
    }
}
