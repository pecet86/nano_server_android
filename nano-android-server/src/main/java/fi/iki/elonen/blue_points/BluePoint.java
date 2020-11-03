package fi.iki.elonen.blue_points;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.blue_points.annotations.BluePointRoute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static fi.iki.elonen.blue_points.Utils.normalizeUri;

@EqualsAndHashCode
public class BluePoint implements Comparable<BluePoint> {

    private static final String TAG = BluePoint.class.getSimpleName();

    private static final Pattern PARAM_PATTERN = Pattern.compile("(?<=(^|/)):[a-zA-Z0-9_-]+(?=(/|$))");
    private static final String PARAM_MATCHER = "([A-Za-z0-9\\-\\._~:/?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=\\s]+)";

    @Getter
    private final String uri;
    @Getter
    private final Method method;
    @EqualsAndHashCode.Exclude
    private final RouterNanoHTTPD router;
    @EqualsAndHashCode.Exclude
    private final Pattern uriPattern;
    @EqualsAndHashCode.Exclude
    private final java.lang.reflect.Method handler;
    private final List<String> uriParams = new ArrayList<>();
    @Setter
    @Getter
    private int priority;

    BluePoint(RouterNanoHTTPD router, BluePointRoute route, @NonNull java.lang.reflect.Method handler) {
        this.router = router;
        method = route.method();
        this.handler = handler;
        priority = route.priority() != -1 ? route.priority() : 0;
        if (route.path().isEmpty()) {
            uri = null;
            uriPattern = null;
        } else {
            uri = normalizeUri(route.path());
            uriPattern = createUriPattern();
        }
    }

    BluePoint(RouterNanoHTTPD router, @NonNull java.lang.reflect.Method handler) {
        this.router = router;
        this.handler = handler;
        uriPattern = null;
        uri = null;
        method = null;
    }

    private Pattern createUriPattern() {
        String patternUri = uri;
        Matcher matcher = PARAM_PATTERN.matcher(patternUri);
        int start = 0;
        while (matcher.find(start)) {
            uriParams.add(patternUri.substring(matcher.start() + 1, matcher.end()));
            patternUri = new StringBuilder()
                    .append(patternUri.substring(0, matcher.start()))
                    .append(PARAM_MATCHER)
                    .append(patternUri.substring(matcher.end()))
                    .toString();
            start = matcher.start() + PARAM_MATCHER.length();
            matcher = PARAM_PATTERN.matcher(patternUri);
        }
        return Pattern.compile(patternUri);
    }

    public Response process(Map<String, String> urlParams, IHTTPSession session) {
        String error;
        try {
            Object object = router.getProxy();
            if (handler.getParameterCount() == 0) {
                return (Response) handler.invoke(object);
            } else {
                Request request = Request
                        .builder()
                        .bluePoint(this)
                        .session(session)
                        .urlParams(urlParams)
                        .build();
                //log("process", String.format("request: %s", request));
                return (Response) handler.invoke(object, request);
            }
        } catch (Exception ex) {
            error = "Error: " + ex.getClass().getName() + " : " + ex.getMessage();
            log("process", ex.getMessage(), ex);
        }
        return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", error);
    }

    public Map<String, String> match(@NonNull String url, @NonNull Method method) {
        Matcher matcher = uriPattern.matcher(url);
        if (matcher.matches() && this.method == method) {
            if (uriParams.size() > 0) {
                Map<String, String> result = new HashMap<>();
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    result.put(uriParams.get(i - 1), matcher.group(i));
                }
                return result;
            } else {
                return Collections.emptyMap();
            }
        }
        return null;
    }

    @Override
    public int compareTo(BluePoint that) {
        if (that == null) {
            return 1;
        } else if (priority > that.priority) {
            return 1;
        } else if (priority < that.priority) {
            return -1;
        } else {
            return 0;
        }
    }

    //<editor-fold desc="logs">
    protected void log(String path, String value) {
        Utils.log(TAG, path, value, Log.DEBUG);
    }

    protected void log(String path, String value, int priority) {
        Utils.log(TAG, path, value, priority);
    }

    protected void log(String path, String value, Throwable th) {
        Utils.log(TAG, path, value, th);
    }
    //</editor-fold>
}
