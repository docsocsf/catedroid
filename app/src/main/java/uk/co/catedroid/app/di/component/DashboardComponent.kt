package uk.co.catedroid.app.di.component

import javax.inject.Singleton

import dagger.Component
import uk.co.catedroid.app.di.module.TimetableRepoModule
import uk.co.catedroid.app.di.module.UserInfoRepoModule
import uk.co.catedroid.app.viewmodel.DashboardViewModel

@Singleton
@Component(modules = [UserInfoRepoModule::class, TimetableRepoModule::class])
interface DashboardComponent {
    fun inject(vm: DashboardViewModel)
}
