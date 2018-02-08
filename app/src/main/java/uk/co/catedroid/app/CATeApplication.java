package uk.co.catedroid.app;

import android.app.Application;

import uk.co.catedroid.app.di.DaggerNetComponent;
import uk.co.catedroid.app.di.DaggerUserInfoRepoComponent;
import uk.co.catedroid.app.di.NetComponent;
import uk.co.catedroid.app.di.NetModule;
import uk.co.catedroid.app.di.SharedPreferencesModule;
import uk.co.catedroid.app.di.UserInfoRepoComponent;
import uk.co.catedroid.app.di.UserInfoRepoModule;

public class CATeApplication extends Application {

    private NetComponent netComponent;
    private UserInfoRepoComponent userInfoRepoComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(this))
                .build();

        userInfoRepoComponent = DaggerUserInfoRepoComponent.builder()
                .userInfoRepoModule(new UserInfoRepoModule(this))
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public UserInfoRepoComponent getUserInfoRepoComponent() {
        return userInfoRepoComponent;
    }
}
