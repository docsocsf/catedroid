package uk.co.catedroid.app.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import javax.inject.Inject

import uk.co.catedroid.app.CATeApplication
import uk.co.catedroid.app.data.model.Note
import uk.co.catedroid.app.data.repo.NotesRepo

class NotesListViewModel(application: Application) : AndroidViewModel(application) {

    var notes: LiveData<List<Note>>? = null
        get() {
            if (field == null) {
                notes = notesRepo.notes
            }
            return field
        }
        private set

    @Inject
    lateinit var notesRepo: NotesRepo

    init {
        (application as CATeApplication).notesComponent.inject(this)
    }
}
