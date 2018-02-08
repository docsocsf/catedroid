package uk.co.catedroid.app.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesModule(Application application) {
        this.sharedPreferences = application.getSharedPreferences("catedroid", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
        return sharedPreferences;
    }
}
