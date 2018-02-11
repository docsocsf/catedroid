package uk.co.catedroid.app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.data.model.Exercise;
import uk.co.catedroid.app.data.model.UserInfo;
import uk.co.catedroid.app.viewmodel.DashboardViewModel;

public class DashboardFragment extends Fragment {

    @BindView(R.id.dashboard_userinfo_greeting) protected TextView greetingText;
    @BindView(R.id.dashboard_userinfo_text) protected TextView userInfoText;
    @BindView(R.id.dashboard_timetable_outstanding_exercises_text) protected TextView outstandingExercisesText;
    @BindView(R.id.dashboard_timetable_outstanding_exercises_progress) protected ProgressBar outstandingExercisesProgress;
    @BindView(R.id.dashboard_timetable_list) protected RecyclerView recyclerView;

    private DashboardViewModel viewModel;

    private List<Exercise> exercises;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
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

        return rootView;
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
            if (!e.getSubmissionStatus().equals("OK")) {
                outstandingExercises.add(e);
            }
        }

        if (this.exercises == null) {
            this.exercises = exercises;
            DashboardTimetableAdapter timetableAdapter = new DashboardTimetableAdapter(getContext(),
                    outstandingExercises,
                    new DashboardTimetableAdapter.DashboardTimetableItemClickedListener() {
                @Override
                public void onClick(Exercise e) {
                    dashboardExerciseClicked(e);
                }
            });
            recyclerView.setAdapter(timetableAdapter);
        }

        outstandingExercisesText.setText(getResources().getString(
                R.string.ui_dashboard_outstanding_exercises_format, outstandingExercises.size()));
        outstandingExercisesProgress.setVisibility(View.GONE);
    }

    private void dashboardExerciseClicked(Exercise exercise) {
        viewModel.exerciseClicked(exercise);
    }
}
