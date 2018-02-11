package uk.co.catedroid.app;

import android.app.Application;

import uk.co.catedroid.app.di.component.DaggerDashboardComponent;
import uk.co.catedroid.app.di.component.DaggerNetComponent;
import uk.co.catedroid.app.di.component.DaggerNotesComponent;
import uk.co.catedroid.app.di.component.DashboardComponent;
import uk.co.catedroid.app.di.component.NetComponent;
import uk.co.catedroid.app.di.component.NotesComponent;
import uk.co.catedroid.app.di.module.NetModule;
import uk.co.catedroid.app.di.module.NotesRepoModule;
import uk.co.catedroid.app.di.module.SharedPreferencesModule;
import uk.co.catedroid.app.di.module.TimetableRepoModule;
import uk.co.catedroid.app.di.module.UserInfoRepoModule;

public class CATeApplication extends Application {

    private NetComponent netComponent;
    private DashboardComponent dashboardComponent;
    private NotesComponent notesComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(this))
                .build();

        dashboardComponent = DaggerDashboardComponent.builder()
                .userInfoRepoModule(new UserInfoRepoModule(this))
                .timetableRepoModule(new TimetableRepoModule(this))
                .build();

        notesComponent = DaggerNotesComponent.builder()
                .notesRepoModule(new NotesRepoModule(this))
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public DashboardComponent getDashboardComponent() {
        return dashboardComponent;
    }

    public NotesComponent getNotesComponent() {
        return notesComponent;
    }
}
