package pl.pecet.nano_android_server;

import android.util.Log;

import java.util.List;

import fi.iki.elonen.router.RouterNanoHTTPD;
import lombok.Builder;
import lombok.Getter;

public abstract class NanoAndroidServer extends RouterNanoHTTPD {

    protected String TAG;

    {
        TAG = getClass().getSimpleName();
    }

    public NanoAndroidServer() {
        super(8080);
        addMappings();
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
    //</editor-fold>

    protected void log(String path, String value, Throwable th) {
        log(TAG, path, value, th);
    }

    @Override
    public void addMappings() {
        super.addMappings();

        getMappingsRoute().forEach(this::addRoute);
    }

    @Override
    public Response serve(IHTTPSession session) {
        log("serve", String.format("init %s", session.getUri()));
        return super.serve(session);
    }

    protected void addRoute(Route<UriResponder> route) {
        super.addRoute(route.url, route.handler, route.initParameter);
    }

    public abstract List<Route<UriResponder>> getMappingsRoute();

    @Builder
    @Getter
    protected static class Route<T extends UriResponder> {
        private final String url; //"/tile/:name/:z/:y/:x/file.png"
        private final Class<? extends T> handler;
        private final Object[] initParameter;
    }
}
