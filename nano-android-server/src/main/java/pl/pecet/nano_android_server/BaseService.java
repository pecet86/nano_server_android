package pl.pecet.nano_android_server;

import android.app.Service;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public abstract class BaseService extends Service {

    public static final String MODE_KEY = "MODE";
    protected String TAG;

    {
        TAG = getClass().getSimpleName();
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

    //<editor-fold desc="onStartCommand">
    protected int _onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String mode = intent == null ? null : intent.getStringExtra(MODE_KEY);
        log("onStartCommand", "init: " + mode);

        return onStartCommand(mode, intent, flags, startId);
    }

    protected abstract int onStartCommand(@Nullable String mode, @Nullable Intent intent, int flags, int startId);
    //</editor-fold>

    protected void stopSelf2() {
        log("stopSelf", "ok");
        stopSelf();
    }
}
