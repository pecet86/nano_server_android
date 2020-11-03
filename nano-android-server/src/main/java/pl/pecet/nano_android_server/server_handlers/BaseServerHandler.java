package pl.pecet.nano_android_server.server_handlers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;
import pl.pecet.nano_android_server.BuildConfig;

public abstract class BaseServerHandler implements RouterNanoHTTPD.UriResponder {

    protected static final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss 'GMT'Z '('z')'", Locale.ENGLISH);

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

    //<editor-fold desc="base">
    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return forbidden();
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return forbidden();
    }

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return forbidden();
    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return forbidden();
    }

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return forbidden();
    }

    private Response forbidden() {
        return NanoHTTPD.newFixedLengthResponse(Status.FORBIDDEN, "text/html", "forbidden");
    }
    //</editor-fold>
}
