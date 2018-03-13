package uk.co.catedroid.app.di.module

import android.app.Application

import javax.inject.Inject
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.data.repo.TimetableRepository

@Module(includes = [NetModule::class])
class TimetableRepoModule(application: Application) {
    private val timetableRepository: TimetableRepository

    @Inject
    lateinit var service: CateService

    init {
        (application as CATeApplication).netComponent.inject(this)
        timetableRepository = TimetableRepository(application, service)
    }

    @Provides
    @Singleton
    internal fun providesTimetableRepository(): TimetableRepository {
        return timetableRepository
    }
}
