package uk.co.catedroid.app.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import uk.co.catedroid.app.data.model.Exercise;
import uk.co.catedroid.app.data.model.UserInfo;

public interface CateService {
    @POST("info")
    Call<UserInfo> getInfo();

    @POST("timetable")
    Call<List<Exercise>> getTimetable();
}
