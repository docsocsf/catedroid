package uk.co.catedroid.app.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import javax.inject.Inject

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.catedroid.app.api.CateService
import uk.co.catedroid.app.data.model.Exercise

class TimetableRepository @Inject
constructor(private val context: Context, private val cateService: CateService) {

    val timetable: LiveData<List<Exercise>>
        get() {
            val data = MutableLiveData<List<Exercise>>()
            cateService.timetable.enqueue(object : Callback<List<Exercise>> {
                override fun onResponse(call: Call<List<Exercise>>,
                                        response: Response<List<Exercise>>) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                    Log.e("CATe", "TimetableRepository - Failed to get timetable: " +
                            t.message, t)
                }
            })

            return data
        }

    fun downloadSpec(e: Exercise, lv: MutableLiveData<File>) {
        if (e.specKey == null) {
            Log.w("CATe", "Attempt to download exercise spec with no spec key")
            return
        }

        Log.d("CATe", "Downloading spec: ${e.specKey}")
        cateService.getFile(e.specKey).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()

                if (body == null) {
                    Log.e("CATe", "Spec has a null response body")
                    return
                }

                val outputFile: File
                val contentDisposition = response.headers()["Content-Disposition"]
                val outputFilename =
                        contentDisposition?.substring(22, contentDisposition.length - 1)

                val inputStream: InputStream
                val outputStream: OutputStream

                try {
                    outputFile = File(context.cacheDir, outputFilename)

                    val fileReader = ByteArray(4096)

                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(outputFile)

                    while (true) {
                        val read = inputStream.read(fileReader)

                        if (read == -1) {
                            break
                        }

                        outputStream.write(fileReader, 0, read)
                    }

                    outputStream.flush()
                } catch (e: IOException) {
                    Log.e("CATe", "IOException whilst saving spec", e)
                    return
                }

                lv.value = outputFile
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("CATe", "Couldn't get spec ${e.specKey}")
            }
        })
    }
}
