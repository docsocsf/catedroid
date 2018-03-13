package uk.co.catedroid.app.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import uk.co.catedroid.app.data.model.Exercise
import uk.co.catedroid.app.data.model.Note
import uk.co.catedroid.app.data.model.UserInfo

interface CateService {
    @get:POST("info")
    val info: Call<UserInfo>

    @get:POST("timetable")
    val timetable: Call<List<Exercise>>

    @get:POST("notes")
    val notes: Call<List<Note>>

    @FormUrlEncoded
    @POST("file")
    fun getFile(@Field("filekey") fileKey: String): Call<ResponseBody>
}
