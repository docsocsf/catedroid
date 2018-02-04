package uk.co.catedroid.app;


import android.app.Application;

import uk.co.catedroid.app.di.DaggerNetComponent;
import uk.co.catedroid.app.di.NetComponent;
import uk.co.catedroid.app.di.NetModule;

public class CATeApplication extends Application {

    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(this))
                .build();

    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
