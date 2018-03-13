package uk.co.catedroid.app.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log

import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.data.model.UserInfo

class UserInfoRepository @Inject
constructor(private val cateService: CateService) {

    val user: LiveData<UserInfo>
        get() {
            val data = MutableLiveData<UserInfo>()
            cateService.info.enqueue(object : Callback<UserInfo> {
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    Log.e("CATe", "UserInfoRepository - Failed to get user info: " + t.message, t)
                }
            })

            return data
        }
}
