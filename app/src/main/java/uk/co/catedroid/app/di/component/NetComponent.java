package uk.co.catedroid.app.di.component;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.catedroid.app.di.module.NetModule;
import uk.co.catedroid.app.di.module.NotesRepoModule;
import uk.co.catedroid.app.di.module.SharedPreferencesModule;
import uk.co.catedroid.app.di.module.TimetableRepoModule;
import uk.co.catedroid.app.di.module.UserInfoRepoModule;
import uk.co.catedroid.app.ui.LaunchActivity;
import uk.co.catedroid.app.ui.MainActivity;
import uk.co.catedroid.app.viewmodel.LoginViewModel;

@Singleton
@Component(modules = {NetModule.class, SharedPreferencesModule.class})
public interface NetComponent {
    void inject(LaunchActivity a);
    void inject(MainActivity a);

    void inject(LoginViewModel vm);

    void inject(TimetableRepoModule m);
    void inject(UserInfoRepoModule m);
    void inject(NotesRepoModule m);
}
