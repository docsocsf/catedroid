package uk.co.catedroid.app.di.module

import android.app.Application

import javax.inject.Inject
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.data.repo.NotesRepo

@Module(includes = [NetModule::class])
class NotesRepoModule(application: Application) {
    private val notesRepo: NotesRepo

    @Inject
    lateinit var service: CateService

    init {
        (application as CATeApplication).netComponent.inject(this)
        notesRepo = NotesRepo(service)
    }

    @Provides
    @Singleton
    internal fun providesNotesRepo(): NotesRepo {
        return notesRepo
    }
}
