package fi.iki.elonen.blue_points;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import lombok.experimental.UtilityClass;
import pl.pecet.nano_android_server.BuildConfig;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.Response.Status;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

@UtilityClass
public class Utils {

    public static final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss 'GMT'Z '('z')'", Locale.ENGLISH);

    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_JSON = "application/json";

    public static Response forbiddenHandler() {
        return newFixedLengthResponse(Status.FORBIDDEN, "text/html", "forbidden");
    }

    public static Response error404Handler() {
        return newFixedLengthResponse(Status.NOT_FOUND, "text/html", "<html><body><h3>Error 404: the requested page doesn't exist.</h3></body></html>");
    }

    public static Response notImplementedHandler() {
        return newFixedLengthResponse(Status.OK, "text/html", "<html><body><h2>The uri is mapped in the router, but no handler is specified. <br> Status: Not implemented!</h3></body></html>");
    }

    public static String normalizeUri(String value) {
        if (value == null) {
            return value;
        }
        if (value.startsWith("/")) {
            value = value.substring(1);
        }
        if (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    public static BufferedInputStream fileToInputStream(File fileOrdirectory) throws IOException {
        return new BufferedInputStream(new FileInputStream(fileOrdirectory));
    }

    public static String[] getPathArray(String uri) {
        return Arrays.stream(uri.split("/")).filter(s -> s.length() > 0).toArray(String[]::new);
    }

    //<editor-fold desc="logs">
    public static void log(String TAG, String path, String value) {
        log(TAG, path, value, Log.DEBUG);
    }

    public static void log(String TAG, String path, String value, int priority) {
        if (BuildConfig.SERVER_LOGS) {
            Log.println(priority, String.format("%s.%s", TAG, path), value);
        }
    }

    public static void log(String TAG, String path, String value, Throwable th) {
        if (BuildConfig.SERVER_LOGS) {
            Log.e(String.format("%s.%s", TAG, path), value, th);
        }
    }
    //</editor-fold>
}
