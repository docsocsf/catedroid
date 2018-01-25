package uk.co.catedroid.app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.catedroid.app.R;
import uk.co.catedroid.app.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private View layoutLoginForm;
    private View layoutInProgress;

    private EditText usernameField;
    private EditText passwordField;
    private TextView errorField;
    private Button loginButton;

    private TextView appInfoField;

    private LoginViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.layoutLoginForm = this.findViewById(R.id.login_layout_login_form);
        this.layoutInProgress = this.findViewById(R.id.login_layout_in_progress);

        this.usernameField = this.findViewById(R.id.login_username_field);
        this.passwordField = this.findViewById(R.id.login_password_field);
        this.errorField = this.findViewById(R.id.login_error_field);
        this.loginButton = this.findViewById(R.id.login_login_button);

        this.appInfoField = this.findViewById(R.id.login_app_info);

        this.model = ViewModelProviders.of(this).get(LoginViewModel.class);

        this.model.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                setErrorText(s);
            }
        });

        this.model.getState().observe(this, new Observer<LoginViewModel.LOGIN_VIEW_STATES>() {
            @Override
            public void onChanged(@Nullable LoginViewModel.LOGIN_VIEW_STATES login_view_states) {
                if(login_view_states != null) {
                    switch (login_view_states) {
                        case LOGIN_FORM:
                            showLoginForm();
                            break;
                        case LOGIN_IN_PROGRESS:
                            showLoginInProgress();
                            break;
                        case LOGGED_IN:
                            goToMainActivity();
                            break;
                    }
                } else {
                    Log.e("CATe", "LoginActivity observed a null state");
                }
            }
        });

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                model.performLogin(username, password);
            }
        });

        // Setting app info text
        String versionText = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("CATe", "LoginActivity: Can't get package info for package ["
                    + getPackageName() + "]");
        }
        this.appInfoField.setText(getResources().getString(R.string.app_info_format,
                getString(R.string.app_name), versionText));
    }

    public void setErrorText(String text) {
        if (text != null && !text.equals("")) {
            errorField.setText(text);
            errorField.setVisibility(View.VISIBLE);
        } else {
            errorField.setText("");
            errorField.setVisibility(View.GONE);
        }
    }

    public void showLoginForm() {
        layoutLoginForm.setVisibility(View.VISIBLE);
        layoutInProgress.setVisibility(View.GONE);
    }

    public void showLoginInProgress() {
        layoutLoginForm.setVisibility(View.GONE);
        layoutInProgress.setVisibility(View.VISIBLE);
    }

    public void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
