package uk.co.catedroid.app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.auth.LoginManager;
import uk.co.catedroid.app.data.model.Exercise;
import uk.co.catedroid.app.data.model.UserInfo;
import uk.co.catedroid.app.viewmodel.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.dashboard_greeting_text) protected TextView greetingText;
    @BindView(R.id.dashboard_user_info_text) protected TextView userInfoText;
    @BindView(R.id.dashboard_timetable_outstanding_exercises_text) protected TextView outstandingExercisesText;
    @BindView(R.id.dashboard_timetable_list) protected RecyclerView recyclerView;

    private List<Exercise> exercises;
    private DashboardTimetableAdapter timetableAdapter;

    private DashboardViewModel viewModel;

    @Inject
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);

        ((CATeApplication) getApplication()).getNetComponent().inject(this);

        LoginManager loginManager = new LoginManager(sharedPrefs);
        String uname = loginManager.getLogin().getUsername();
        Toast.makeText(this, "Logged in as: " + uname, Toast.LENGTH_SHORT).show();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        viewModel.getUserInfo().observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(@Nullable UserInfo userInfo) {
                updateUserInfo(userInfo);
            }
        });

        viewModel.getTimetable().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                if (exercises == null) {
                    Log.e("CATe", "Timetable update NULL");
                    return;
                }
                updateTimetableInfo(exercises);
            }
        });
    }

    private void updateUserInfo(UserInfo userInfo) {
        String firstName = userInfo.getName().split(" ")[0];
        greetingText.setText(
                getResources().getString(R.string.ui_dashboard_greeting_format, firstName));
        userInfoText.setText(getResources().getString(R.string.ui_dashboard_user_info_format, userInfo.getLogin(), userInfo.getCid()));
    }

    private void updateTimetableInfo(List<Exercise> exercises) {
        List<Exercise> outstandingExercises = new ArrayList<>();

        for (Exercise e : exercises) {
            if (e.getSubmisisonStatus().equals("N-S")) {
                outstandingExercises.add(e);
            }
        }

        if (this.exercises == null) {
            this.exercises = exercises;
            timetableAdapter = new DashboardTimetableAdapter(this, outstandingExercises);
            recyclerView.setAdapter(timetableAdapter);
        }



        outstandingExercisesText.setText(getResources().getString(
                R.string.ui_dashboard_outstanding_exercises_format, outstandingExercises.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logout:
                LoginManager loginManager = new LoginManager(sharedPrefs);
                loginManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
