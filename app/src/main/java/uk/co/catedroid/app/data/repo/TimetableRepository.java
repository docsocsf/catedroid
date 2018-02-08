package uk.co.catedroid.app.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.data.model.Exercise;

public class TimetableRepository {
    private CateService cateService;

    @Inject
    public TimetableRepository(CateService cateService) {
        this.cateService = cateService;
    }

    public LiveData<List<Exercise>> getTimetable() {
        final MutableLiveData<List<Exercise>> data = new MutableLiveData<>();
        cateService.getTimetable().enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                Log.e("CATe", "welp");
            }
        });

        return data;
    }
}
