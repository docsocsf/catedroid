package uk.co.catedroid.app.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import kotlinx.android.synthetic.main.fragment_dashboard.*

import uk.co.catedroid.app.R
import uk.co.catedroid.app.data.model.Exercise
import uk.co.catedroid.app.data.model.UserInfo
import uk.co.catedroid.app.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        dashboard_timetable_list.layoutManager = llm
        dashboard_timetable_list.setHasFixedSize(true)
        dashboard_timetable_list.isNestedScrollingEnabled = false

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        viewModel.userInfo.observe(this, Observer { userInfo ->
            if (userInfo != null) {
                updateUserInfo(userInfo)
            } else {
                Log.w("CATe", "UserInfo update null")
            }
        })

        viewModel.timetable.observe(this, Observer { exercises ->
            if (exercises != null) {
                updateTimetableInfo(exercises)
            } else {
                Log.e("CATe", "Timetable update NULL")
            }
        })

        viewModel.specFile.observe(this, Observer { specFile ->
            Log.d("CATe", "Spec downloaded: $specFile")

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(FileProvider.getUriForFile(context,
                    "${context.applicationContext.packageName}.uk.co.catedroid.app.provider",
                    specFile), "application/pdf")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        })
    }

    private fun updateUserInfo(userInfo: UserInfo) {
        dashboard_userinfo_greeting.text = resources.getString(
                R.string.ui_dashboard_greeting_format, userInfo.name)
        dashboard_userinfo_text.text = resources.getString(
                R.string.ui_dashboard_user_info_format, userInfo.login, userInfo.cid)
    }

    private fun updateTimetableInfo(exercises: List<Exercise>) {
        val outstandingExercises = ArrayList<Exercise>()
        exercises.filter { it.submissionStatus != "OK" }.forEach { outstandingExercises.add(it) }

        dashboard_timetable_list.adapter = DashboardTimetableAdapter(
                context, outstandingExercises,
                { viewModel.exerciseClicked(it) },
                { Log.d("CATe", "${it.code} ${it.name} Hand in") })

        val outstandingExercisesSize = outstandingExercises.size
        dashboard_timetable_outstanding_exercises_text.text = resources.getQuantityString(R.plurals.ui_dashboard_timetable_outstanding_exercises_format, outstandingExercisesSize, outstandingExercisesSize)
        dashboard_timetable_outstanding_exercises_progress.visibility = View.GONE
    }
}
