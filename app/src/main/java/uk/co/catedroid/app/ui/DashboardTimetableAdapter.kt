package uk.co.catedroid.app.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import kotlinx.android.synthetic.main.item_dashboard_timetable.view.*

import uk.co.catedroid.app.R
import uk.co.catedroid.app.data.model.Exercise

class DashboardTimetableAdapter internal constructor(
        private val context: Context, private val exercises: List<Exercise>,
        private val onSpecClicked: (Exercise) -> Unit,
        private val onHandInClicked: (Exercise) -> Unit) :
        RecyclerView.Adapter<DashboardTimetableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardTimetableAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_dashboard_timetable, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val exercise = exercises[position]

        viewHolder.setExercise(exercise)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var exercise: Exercise

        init {
            itemView.dashboard_timetable_item_exercise_spec_button.setOnClickListener {
                Log.d("CATe", "Spec button clicked! " + exercise.name)
                itemView.dashboard_timetable_item_exercise_spec_button.isEnabled = false
                onSpecClicked(exercise)
            }

            itemView.dashboard_timetable_item_exercise_handin_button.setOnClickListener {
                Log.d("CATe", "HandIn button clicked! " + exercise.name)
                onHandInClicked(exercise)
            }
        }

        fun setExercise(e: Exercise) {
            exercise = e

            // Set item background according to assessed status
            val assessedBGResource: Int = when (exercise.assessedStatus) {
                "UA-SR" -> R.drawable.timetable_item_unassessed_submission_required
                "A-I" -> R.drawable.timetable_item_assessed_individual
                "A-G" -> R.drawable.timetable_item_assessed_group
                else -> R.drawable.timetable_item_unassessed
            }

            // Set submission status indicator
            var submissionBGResource = assessedBGResource
            var submissionDueSoonBGResource = assessedBGResource
            when (exercise.submissionStatus) {
                "N-S-DS" -> {
                    submissionDueSoonBGResource = R.color.cate_exercise_not_submitted
                    submissionBGResource = R.color.cate_exercise_not_submitted
                }
                "N-S" -> submissionBGResource = R.color.cate_exercise_not_submitted
                "I-DS" -> {
                    submissionDueSoonBGResource = R.color.cate_exercise_incomplete_submission
                    submissionBGResource = R.color.cate_exercise_incomplete_submission
                }
                "I" -> submissionBGResource = R.color.cate_exercise_incomplete_submission
            }

            itemView.dashboard_timetable_item_layout.setBackgroundResource(assessedBGResource)
            itemView.dashboard_timetable_item_submission_indicator.setBackgroundResource(submissionBGResource)
            itemView.dashboard_timetable_item_submission_indicator_due_soon.setBackgroundResource(submissionDueSoonBGResource)

            itemView.dashboard_timetable_item_code.text = exercise.code
            itemView.dashboard_timetable_item_name.text = exercise.name
            itemView.dashboard_timetable_item_module.text = context.resources.getString(
                    R.string.ui_dashboard_timetable_item_module_format,
                    exercise.moduleNumber, exercise.moduleName
            )
            itemView.dashboard_timetable_item_enddate.text = context.resources.getString(R.string.ui_dashboard_timetable_item_enddate_format, exercise.end)

            if (exercise.specKey == null) {
                val specButton: Button = itemView.dashboard_timetable_item_exercise_spec_button
                specButton.isEnabled = false
                specButton.text = context.resources
                        .getString(R.string.ui_dashboard_timetable_item_spec_button_disabled)
            }

            if (exercise.links?.containsKey("handin") != true) {
                itemView.dashboard_timetable_item_exercise_handin_button.visibility = View.GONE
            }
        }
    }
}
