package uk.co.catedroid.app.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log

import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.data.model.Note

class NotesRepo @Inject
constructor(private val cateService: CateService) {

    val notes: LiveData<List<Note>>
        get() {
            val data = MutableLiveData<List<Note>>()
            cateService.notes.enqueue(object : Callback<List<Note>> {
                override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                    Log.e("CATe", "NotesRepo - Failed to get notes: " + t.message, t)
                }
            })

            return data
        }
}
