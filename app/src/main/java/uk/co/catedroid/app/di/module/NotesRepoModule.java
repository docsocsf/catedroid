package uk.co.catedroid.app.di.module;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.data.repo.NotesRepo;

@Module(includes = NetModule.class)
public class NotesRepoModule {
    private NotesRepo notesRepo;

    @Inject
    CateService service;

    public NotesRepoModule(Application application) {
        ((CATeApplication) application).getNetComponent().inject(this);
        notesRepo = new NotesRepo(service);
    }

    @Provides
    @Singleton
    NotesRepo providesNotesRepo() {
        return notesRepo;
    }
}
