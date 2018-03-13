package uk.co.catedroid.app.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import uk.co.catedroid.app.R
import uk.co.catedroid.app.data.model.Exercise

class DashboardTimetableAdapter internal constructor(private val context: Context, private val exercises: List<Exercise>, private val listener: DashboardTimetableItemClickedListener): RecyclerView.Adapter<DashboardTimetableAdapter.ViewHolder>() {

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
        private var exercise: Exercise? = null

        @BindView(R.id.dashboard_timetable_item_layout)
        lateinit var itemLayout: View
        @BindView(R.id.dashboard_timetable_item_submission_indicator)
        lateinit var submissionIndicator: View
        @BindView(R.id.dashboard_timetable_item_submission_indicator_due_soon)
        lateinit var submissionDueSoonIndicator: View
        @BindView(R.id.dashboard_timetable_item_code)
        lateinit var codeText: TextView
        @BindView(R.id.dashboard_timetable_item_name)
        lateinit var nameText: TextView
        @BindView(R.id.dashboard_timetable_item_module)
        lateinit var moduleText: TextView
        @BindView(R.id.dashboard_timetable_item_enddate)
        lateinit var endDateText: TextView
        @BindView(R.id.dashboard_timetable_item_progress)
        lateinit var progress: ProgressBar

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setExercise(e: Exercise) {
            exercise = e

            codeText.text = exercise!!.code
            nameText.text = exercise!!.name
            moduleText.text = context.resources.getString(
                    R.string.ui_dashboard_timetable_item_module_format,
                    exercise!!.moduleNumber, exercise!!.moduleName
            )
            endDateText.text = context.resources.getString(R.string.ui_dashboard_timetable_item_enddate_format, exercise!!.end)

            // Set item background according to assessed status
            val assessedBGResource: Int = when (exercise!!.assessedStatus) {
                "UA-SR" -> R.drawable.timetable_item_unassessed_submission_required
                "A-I" -> R.drawable.timetable_item_assessed_individual
                "A-G" -> R.drawable.timetable_item_assessed_group
                else -> R.drawable.timetable_item_unassessed
            }

            // Set submission status indicator
            var submissionBGResource = assessedBGResource
            var submissionDueSoonBGResource = assessedBGResource
            when (exercise!!.submissionStatus) {
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

            itemLayout.setBackgroundResource(assessedBGResource)
            submissionIndicator.setBackgroundResource(submissionBGResource)
            submissionDueSoonIndicator.setBackgroundResource(submissionDueSoonBGResource)
        }

        @OnClick(R.id.dashboard_timetable_item_layout)
        fun itemClicked() {
            Log.d("CATe", "Timetable item clicked! " + nameText.text)
            listener.onClick(exercise)
        }
    }

    interface DashboardTimetableItemClickedListener {
        fun onClick(e: Exercise?)
    }
}
