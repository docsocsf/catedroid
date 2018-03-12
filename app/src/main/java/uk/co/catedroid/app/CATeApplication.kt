package uk.co.catedroid.app

import android.app.Application

import uk.co.catedroid.app.di.component.DaggerDashboardComponent
import uk.co.catedroid.app.di.component.DaggerNetComponent
import uk.co.catedroid.app.di.component.DaggerNotesComponent
import uk.co.catedroid.app.di.component.DashboardComponent
import uk.co.catedroid.app.di.component.NetComponent
import uk.co.catedroid.app.di.component.NotesComponent
import uk.co.catedroid.app.di.module.NetModule
import uk.co.catedroid.app.di.module.NotesRepoModule
import uk.co.catedroid.app.di.module.SharedPreferencesModule
import uk.co.catedroid.app.di.module.TimetableRepoModule
import uk.co.catedroid.app.di.module.UserInfoRepoModule

class CATeApplication : Application() {

    lateinit var netComponent: NetComponent
        private set
    lateinit var dashboardComponent: DashboardComponent
        private set
    lateinit var notesComponent: NotesComponent
        private set

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
                .netModule(NetModule(this))
                .sharedPreferencesModule(SharedPreferencesModule(this))
                .build()

        dashboardComponent = DaggerDashboardComponent.builder()
                .userInfoRepoModule(UserInfoRepoModule(this))
                .timetableRepoModule(TimetableRepoModule(this))
                .build()

        notesComponent = DaggerNotesComponent.builder()
                .notesRepoModule(NotesRepoModule(this))
                .build()
    }
}
