package uk.co.catedroid.app.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import uk.co.catedroid.app.data.model.Exercise;
import uk.co.catedroid.app.data.model.Note;
import uk.co.catedroid.app.data.model.UserInfo;

public interface CateService {
    @POST("info")
    Call<UserInfo> getInfo();

    @POST("timetable")
    Call<List<Exercise>> getTimetable();

    @POST("notes")
    Call<List<Note>> getNotes();

    @FormUrlEncoded
    @POST("file")
    Call<ResponseBody> getFile(@Field("filekey") String fileKey);
}
