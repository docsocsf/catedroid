package uk.co.catedroid.app.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule(application: Application) {
    private val sharedPreferences: SharedPreferences =
            application.getSharedPreferences("catedroid", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun providesSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}
