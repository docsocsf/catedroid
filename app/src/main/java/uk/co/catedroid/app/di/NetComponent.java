package uk.co.catedroid.app.di;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.catedroid.app.ui.LaunchActivity;
import uk.co.catedroid.app.ui.MainActivity;
import uk.co.catedroid.app.viewmodel.LoginViewModel;

@Singleton
@Component(modules = {NetModule.class, SharedPreferencesModule.class})
public interface NetComponent {
    void inject(LaunchActivity a);
    void inject(MainActivity a);
    void inject(LoginViewModel vm);
    void inject(UserInfoRepoModule uirm);
}
