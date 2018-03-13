package uk.co.catedroid.app.di.module

import android.app.Application

import javax.inject.Inject
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.data.repo.UserInfoRepository

@Module(includes = [NetModule::class])
class UserInfoRepoModule(application: Application) {
    private val userInfoRepository: UserInfoRepository

    @Inject
    lateinit var service: CateService

    init {
        (application as CATeApplication).netComponent.inject(this)
        userInfoRepository = UserInfoRepository(service)
    }

    @Provides
    @Singleton
    internal fun providesUserInfoRepository(): UserInfoRepository {
        return userInfoRepository
    }
}
