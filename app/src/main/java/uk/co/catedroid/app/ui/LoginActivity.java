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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_layout_login_form) protected View layoutLoginForm;
    @BindView(R.id.login_layout_in_progress) protected View layoutInProgress;

    @BindView(R.id.login_username_field) protected EditText usernameField;
    @BindView(R.id.login_password_field) protected EditText passwordField;
    @BindView(R.id.login_error_field) protected TextView errorField;
    @BindView(R.id.login_login_button) protected Button loginButton;

    @BindView(R.id.login_app_info) protected TextView appInfoField;

    private LoginViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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

    @OnClick(R.id.login_login_button)
    protected void doLogin() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        model.performLogin(username, password);
    }

    private void setErrorText(String text) {
        if (text != null && !text.equals("")) {
            errorField.setText(text);
            errorField.setVisibility(View.VISIBLE);
        } else {
            errorField.setText("");
            errorField.setVisibility(View.GONE);
        }
    }

    private void showLoginForm() {
        layoutLoginForm.setVisibility(View.VISIBLE);
        layoutInProgress.setVisibility(View.GONE);
    }

    private void showLoginInProgress() {
        layoutLoginForm.setVisibility(View.GONE);
        layoutInProgress.setVisibility(View.VISIBLE);
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
