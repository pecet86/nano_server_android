package pl.pecet.nano_server_android.android;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.pecet.nano_android_server.BuildConfig;
import pl.pecet.nano_server_android.MainActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiService {

    protected static final String TAG = MainActivity.class.getSimpleName();
    private final Api apiService;
    private final Context context;

    public ApiService(Context context) {
        this.context = context;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));


        Retrofit retrofit2 = new Retrofit
                .Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build())
                .build();

        apiService = retrofit2.create(Api.class);
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .create();
    }

    //<editor-fold desc="logs">
    protected static void log(String TAG, String path, String value) {
        log(TAG, path, value, Log.DEBUG);
    }

    protected static void log(String TAG, String path, String value, int priority) {
        if (BuildConfig.SERVER_LOGS) {
            Log.println(priority, String.format("%s.%s", TAG, path), value);
        }
    }

    protected static void log(String TAG, String path, String value, Throwable th) {
        if (BuildConfig.SERVER_LOGS) {
            Log.e(String.format("%s.%s", TAG, path), value, th);
        }
    }

    protected void log(String path, String value) {
        log(TAG, path, value, Log.DEBUG);
    }

    protected void log(String path, String value, int priority) {
        log(TAG, path, value, priority);
    }

    protected void log(String path, String value, Throwable th) {
        log(TAG, path, value, th);
    }
    //</editor-fold>

    public JsonObject getTestSync() throws IOException {
        Call<JsonObject> call = apiService.getTest(
                "xxx",
                "nazwa",
                "qqq",
                10
        );

        Response<JsonObject> response = call.execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            return null;
        }
    }

    public Maybe<JsonObject> getTest() {
        return Maybe.create((MaybeOnSubscribe<JsonObject>) emitter -> {
            JsonObject result = getTestSync();
            if (result == null) {
                emitter.onComplete();
            } else {
                emitter.onSuccess(result);
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public JsonObject postTestSync() throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("XXX", "ddd");

        Call<JsonObject> call = apiService.postTest(
                "xxx",
                "nazwa",
                "qqq",
                10,
                body
        );

        Response<JsonObject> response = call.execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            return null;
        }
    }

    public Maybe<JsonObject> postTest() {
        return Maybe.create((MaybeOnSubscribe<JsonObject>) emitter -> {
            JsonObject result = postTestSync();
            if (result == null) {
                emitter.onComplete();
            } else {
                emitter.onSuccess(result);
            }
        })
                .subscribeOn(Schedulers.io());
    }
}
