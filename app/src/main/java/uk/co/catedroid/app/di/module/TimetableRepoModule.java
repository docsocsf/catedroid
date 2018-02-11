package uk.co.catedroid.app.di.module;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.data.repo.TimetableRepository;

@Module(includes = NetModule.class)
public class TimetableRepoModule {
    private TimetableRepository timetableRepository;

    @Inject
    CateService service;

    public TimetableRepoModule(Application application) {
        ((CATeApplication) application).getNetComponent().inject(this);
        timetableRepository = new TimetableRepository(service);
    }

    @Provides
    @Singleton
    TimetableRepository providesTimetableRepository() {
        return timetableRepository;
    }
}
