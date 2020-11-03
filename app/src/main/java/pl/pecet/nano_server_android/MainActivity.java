package pl.pecet.nano_server_android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import pl.pecet.nano_server_android.api.ApiService;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = MainActivity.class.getSimpleName();

    //<editor-fold desc="logs">
    protected static void log(String TAG, String path, String value) {
        log(TAG, path, value, Log.DEBUG);
    }

    protected static void log(String TAG, String path, String value, int priority) {
        Log.println(priority, String.format("%s.%s", TAG, path), value);
    }

    protected static void log(String TAG, String path, String value, Throwable th) {
        Log.e(String.format("%s.%s", TAG, path), value, th);
    }

    @SuppressWarnings("deprecation")
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Objects.equals(serviceClass.getName(), service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected void log(String path, String value) {
        log(TAG, path, value, Log.DEBUG);
    }

    protected void log(String path, String value, int priority) {
        log(TAG, path, value, priority);
    }
    //</editor-fold>

    protected void log(String path, String value, Throwable th) {
        log(TAG, path, value, th);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServerService();

        ApiService service = new ApiService(this);
        Completable
                .timer(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> {
                    log("get", service.getTestSync().toString());
                    log("post", service.postTestSync().toString());
                })
                .onErrorComplete(th -> {
                    log("onCreate", th.getMessage(), th);
                    return true;
                })
                .subscribe();
    }

    @Override
    public void onDestroy() {
        stopServerService();
        super.onDestroy();
    }

    //<editor-fold desc="ServerService">
    private void startServerService() {
        log("startServerService", "init");
        Intent intent = new Intent(getApplicationContext(), ServerServiceImpl.class);
        intent.putExtra("MODE", ServerServiceImpl.MODE_START_SERVER);

        if (isServiceRunning(getApplicationContext(), ServerServiceImpl.class)) {
            log("startServerService", "is run");
            return;
        }

        getApplicationContext().startService(intent);
    }
    //</editor-fold>

    private void stopServerService() {
        Intent intent = new Intent(getApplicationContext(), ServerServiceImpl.class);

        if (isServiceRunning(getApplicationContext(), ServerServiceImpl.class)) {
            stopService(intent);
        }
    }
}