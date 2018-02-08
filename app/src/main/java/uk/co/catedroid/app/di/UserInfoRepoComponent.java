package uk.co.catedroid.app.di;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.catedroid.app.viewmodel.DashboardViewModel;

@Singleton
@Component(modules = {UserInfoRepoModule.class})
public interface UserInfoRepoComponent {
    void inject(DashboardViewModel vm);
}
