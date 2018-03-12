package uk.co.catedroid.app.di.component

import javax.inject.Singleton

import dagger.Component
import uk.co.catedroid.app.di.module.NetModule
import uk.co.catedroid.app.di.module.NotesRepoModule
import uk.co.catedroid.app.di.module.SharedPreferencesModule
import uk.co.catedroid.app.di.module.TimetableRepoModule
import uk.co.catedroid.app.di.module.UserInfoRepoModule
import uk.co.catedroid.app.ui.LaunchActivity
import uk.co.catedroid.app.ui.MainActivity
import uk.co.catedroid.app.viewmodel.LoginViewModel

@Singleton
@Component(modules = [NetModule::class, SharedPreferencesModule::class])
interface NetComponent {
    fun inject(a: LaunchActivity)
    fun inject(a: MainActivity)

    fun inject(vm: LoginViewModel)

    fun inject(m: TimetableRepoModule)
    fun inject(m: UserInfoRepoModule)
    fun inject(m: NotesRepoModule)
}
