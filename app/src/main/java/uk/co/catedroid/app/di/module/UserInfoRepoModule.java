package uk.co.catedroid.app.di.module;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.data.repo.UserInfoRepository;

@Module(includes = NetModule.class)
public class UserInfoRepoModule {
    private UserInfoRepository userInfoRepository;

    @Inject
    CateService service;

    public UserInfoRepoModule(Application application) {
        ((CATeApplication) application).getNetComponent().inject(this);
        userInfoRepository = new UserInfoRepository(service);
    }

    @Provides
    @Singleton
    UserInfoRepository providesUserInfoRepository() {
        return userInfoRepository;
    }
}
