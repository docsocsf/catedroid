package uk.co.catedroid.app.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.catedroid.app.api.CateService;
import uk.co.catedroid.app.data.CateFileProvider;
import uk.co.catedroid.app.data.model.Exercise;

public class TimetableRepository {
    private Context context;
    private CateService cateService;

    @Inject
    public TimetableRepository(Context context, CateService cateService) {
        this.context = context;
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
                Log.e("CATe", "TimetableRepository - Failed to get timetable: "
                        + t.getMessage(), t);
            }
        });

        return data;
    }

    public File downloadSpec(final String specKey) {
        Log.d("CATe", "Downloading spec: " + specKey);
        cateService.getFile(specKey).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                if (body == null) {
                    Log.e("CATe", "Spec response body null");
                }

                File outputFile;
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    File cacheDir = context.getCacheDir();
                    outputFile = File.createTempFile(specKey, "pdf", cacheDir);

                    byte[] fileReader = new byte[4096];

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(outputFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);
                    }

                    outputStream.flush();
                } catch (IOException e) {
                    Log.e("CATe", "IOException whilst saving spec", e);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(CateFileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName()
                                + ".uk.co.catedroid.app.provider",
                        outputFile), "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CATe", "Couldn't get spec " + specKey);
            }
        });

        return null;
    }
}
