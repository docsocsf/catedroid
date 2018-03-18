package uk.co.catedroid.app.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import uk.co.catedroid.app.R
import uk.co.catedroid.app.data.model.Exercise
import uk.co.catedroid.app.data.model.UserInfo
import uk.co.catedroid.app.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    @BindView(R.id.dashboard_userinfo_greeting)
    lateinit var greetingText: TextView
    @BindView(R.id.dashboard_userinfo_text)
    lateinit var userInfoText: TextView
    @BindView(R.id.dashboard_timetable_outstanding_exercises_text)
    lateinit var outstandingExercisesText: TextView
    @BindView(R.id.dashboard_timetable_outstanding_exercises_progress)
    lateinit var outstandingExercisesProgress: ProgressBar
    @BindView(R.id.dashboard_timetable_list)
    lateinit var recyclerView: RecyclerView

    private var viewModel: DashboardViewModel? = null

    private var exercises: List<Exercise>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_dashboard, container, false)
        ButterKnife.bind(this, rootView)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false

        viewModel!!.userInfo.observe(this, Observer { userInfo -> updateUserInfo(userInfo) })

        viewModel!!.timetable.observe(this, Observer { exercises ->
            if (exercises == null) {
                Log.e("CATe", "Timetable update NULL")
                return@Observer
            }
            updateTimetableInfo(exercises)
        })

        viewModel!!.specFile.observe(this, Observer { specFile ->
            Log.d("CATe", "Spec downloaded: $specFile")

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(FileProvider.getUriForFile(context,
                    "${context.applicationContext.packageName}.uk.co.catedroid.app.provider",
                    specFile), "application/pdf")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        })

        return rootView
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        val firstName = userInfo!!.name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        greetingText.text = resources.getString(R.string.ui_dashboard_greeting_format, firstName)
        userInfoText.text = resources.getString(R.string.ui_dashboard_user_info_format, userInfo.login, userInfo.cid)
    }

    private fun updateTimetableInfo(exercises: List<Exercise>?) {
        val outstandingExercises = ArrayList<Exercise>()
        exercises!!
                .filter {it.submissionStatus != "OK"}
                .forEach { outstandingExercises.add(it) }

        if (this.exercises == null) {
            this.exercises = exercises
            val timetableAdapter = DashboardTimetableAdapter(context, outstandingExercises,
                    object : DashboardTimetableAdapter.DashboardTimetableItemClickedListener {
                        override fun onClick(e: Exercise?) {
                            viewModel!!.exerciseClicked(e!!)
                        }
                    })

            recyclerView.adapter = timetableAdapter
        }

        val outstandingExercisesSize = outstandingExercises.size
        outstandingExercisesText.text = resources.getQuantityString(R.plurals.ui_dashboard_timetable_outstanding_exercises_format, outstandingExercisesSize, outstandingExercisesSize)
        outstandingExercisesProgress.visibility = View.GONE
    }
}
