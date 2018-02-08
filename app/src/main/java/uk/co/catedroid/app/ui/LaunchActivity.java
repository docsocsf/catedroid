package uk.co.catedroid.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.auth.LoginManager;

public class LaunchActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((CATeApplication) getApplication()).getNetComponent().inject(this);
        LoginManager loginManager = new LoginManager(sharedPreferences);

        Intent intent;

        if (loginManager.hasStoredCredentials()) {
            intent = new Intent(this, DashboardActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
