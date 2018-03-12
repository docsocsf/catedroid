package uk.co.catedroid.app.di.component

import javax.inject.Singleton

import dagger.Component
import uk.co.catedroid.app.di.module.NotesRepoModule
import uk.co.catedroid.app.viewmodel.NotesListViewModel

@Singleton
@Component(modules = [NotesRepoModule::class])
interface NotesComponent {
    fun inject(vm: NotesListViewModel)
}
