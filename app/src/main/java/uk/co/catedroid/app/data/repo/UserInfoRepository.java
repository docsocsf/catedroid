package uk.co.catedroid.app.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.data.model.UserInfo;

public class UserInfoRepository {
    private CateService cateService;

    @Inject
    public UserInfoRepository(CateService cateService) {
        this.cateService = cateService;
    }

    public LiveData<UserInfo> getUser() {
        final MutableLiveData<UserInfo> data = new MutableLiveData<>();
        cateService.getInfo().enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.e("CATe", "welp");
            }
        });

        return data;
    }
}
