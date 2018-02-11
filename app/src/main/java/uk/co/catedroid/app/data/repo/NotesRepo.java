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
import uk.co.catedroid.app.data.model.Note;

public class NotesRepo {
    private CateService cateService;

    @Inject
    public NotesRepo(CateService cateService) {
        this.cateService = cateService;
    }

    public LiveData<List<Note>> getNotes() {
        final MutableLiveData<List<Note>> data = new MutableLiveData<>();
        cateService.getNotes().enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.e("CATe", "NotesRepo - Failed to get notes: " + t.getMessage(), t);
            }
        });

        return data;
    }
}
