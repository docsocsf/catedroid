package uk.co.catedroid.app.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.data.model.Exercise;

public class DashboardTimetableAdapter extends RecyclerView.Adapter<DashboardTimetableAdapter.ViewHolder> {

    private List<Exercise> exercises;
    private Context context;

    public DashboardTimetableAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @Override
    public DashboardTimetableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_dashboard_timetable, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Exercise exercise = exercises.get(position);

        viewHolder.codeText.setText(exercise.getCode());
        viewHolder.nameText.setText(exercise.getName());
        viewHolder.moduleText.setText(getContext().getResources().getString(
                R.string.ui_dashboard_timetable_item_module_format,
                exercise.getModuleNumber(), exercise.getModuleName()
        ));
        viewHolder.endDateText.setText(getContext().getResources().getString(
                R.string.ui_dashboard_timetable_item_enddate_format, exercise.getEnd()));

        // Set item background according to assessed status
        int assessedBGResource;
        switch (exercise.getAssessedStatus()) {
            case "UA-SR":
                assessedBGResource = R.color.cate_exercise_unassessed_submissionrequired;
                break;
            case "A-I":
                assessedBGResource = R.color.cate_exercise_assessed_individual;
                break;
            case "A-G":
                assessedBGResource = R.color.cate_exercise_assessed_group;
                break;
            default:
                assessedBGResource = R.color.cate_exercise_unassessed;
                break;
        }

        // Set submission status indicator
        int submissionBGResource = assessedBGResource;
        int submissionDueSoonBGResource = assessedBGResource;
        switch (exercise.getSubmissionStatus()) {
            case "N-S-DS":
                submissionDueSoonBGResource = R.color.cate_exercise_not_submitted;
            case "N-S":
                submissionBGResource = R.color.cate_exercise_not_submitted;
                break;
            case "I-DS":
                submissionDueSoonBGResource = R.color.cate_exercise_incomplete_submission;
            case "I":
                submissionBGResource = R.color.cate_exercise_incomplete_submission;
                break;
        }

        viewHolder.itemLayout.setBackgroundResource(assessedBGResource);
        viewHolder.submissionIndicator.setBackgroundResource(submissionBGResource);
        viewHolder.submissionDueSoonIndicator.setBackgroundResource(submissionDueSoonBGResource);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public Context getContext() {
        return context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dashboard_timetable_item_layout) View itemLayout;
        @BindView(R.id.dashboard_timetable_item_submission_indicator) View submissionIndicator;
        @BindView(R.id.dashboard_timetable_item_submission_indicator_due_soon) View submissionDueSoonIndicator;
        @BindView(R.id.dashboard_timetable_item_code) TextView codeText;
        @BindView(R.id.dashboard_timetable_item_name) TextView nameText;
        @BindView(R.id.dashboard_timetable_item_module) TextView moduleText;
        @BindView(R.id.dashboard_timetable_item_enddate) TextView endDateText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
