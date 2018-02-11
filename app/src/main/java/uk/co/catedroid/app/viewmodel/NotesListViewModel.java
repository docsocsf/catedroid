package uk.co.catedroid.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.data.model.Note;
import uk.co.catedroid.app.data.repo.NotesRepo;

public class NotesListViewModel extends AndroidViewModel {

    private LiveData<List<Note>> notes;

    @Inject
    NotesRepo notesRepo;

    public NotesListViewModel(@NonNull Application application) {
        super(application);
        ((CATeApplication) application).getNotesComponent().inject(this);

        notes = notesRepo.getNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}
