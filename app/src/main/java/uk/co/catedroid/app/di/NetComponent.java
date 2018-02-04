package uk.co.catedroid.app.di;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.catedroid.app.viewmodel.LoginViewModel;

@Singleton
@Component(modules = {NetModule.class})
public interface NetComponent {
    void inject(LoginViewModel vm);
}
