package uk.co.catedroid.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import uk.co.catedroid.app.CATeApplication;
import uk.co.catedroid.app.data.model.Exercise;
import uk.co.catedroid.app.data.model.UserInfo;
import uk.co.catedroid.app.data.repo.TimetableRepository;
import uk.co.catedroid.app.data.repo.UserInfoRepository;

public class DashboardViewModel extends AndroidViewModel {

    private LiveData<UserInfo> userInfo;
    private LiveData<List<Exercise>> timetable;

    @Inject
    UserInfoRepository userInfoRepo;
    @Inject
    TimetableRepository timetableRepository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        ((CATeApplication) application).getDashboardComponent().inject(this);

        userInfo = userInfoRepo.getUser();
        timetable = timetableRepository.getTimetable();
    }

    public LiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public LiveData<List<Exercise>> getTimetable() {
        return timetable;
    }

    public void exerciseClicked(Exercise e) {
        timetableRepository.downloadSpec(e);
    }
}
