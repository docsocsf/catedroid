package uk.co.catedroid.app.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import javax.inject.Inject

import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.data.model.Exercise
import uk.co.catedroid.app.data.model.UserInfo
import uk.co.catedroid.app.data.repo.TimetableRepository
import uk.co.catedroid.app.data.repo.UserInfoRepository

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val userInfo: LiveData<UserInfo>
    val timetable: LiveData<List<Exercise>>

    @Inject
    lateinit var userInfoRepo: UserInfoRepository
    @Inject
    lateinit var timetableRepository: TimetableRepository

    init {
        (application as CATeApplication).dashboardComponent.inject(this)

        userInfo = userInfoRepo.user
        timetable = timetableRepository.timetable
    }

    fun exerciseClicked(e: Exercise) {
        timetableRepository.downloadSpec(e)
    }
}
