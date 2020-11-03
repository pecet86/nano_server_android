package pl.pecet.nano_android_server.server_handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import pl.pecet.nano_android_server.BuildConfig;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.Response.IStatus;
import static fi.iki.elonen.NanoHTTPD.Response.Status;
import static pl.pecet.nano_android_server.helper.Utils.forbidden;
import static pl.pecet.nano_android_server.server_handlers.WrapperServerHandler.SingleData;

public class SingleHandler {

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

    @NonNull
    protected IStatus getStatus() {
        return Status.OK;
    }

    @NonNull
    protected String getMimeType() {
        return "text/html";
    }

    @NonNull
    public Response prosess(SingleData data) {
        return forbidden();
    }
}
