package pl.pecet.nano_android_server.server_handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.pecet.nano_android_server.BuildConfig;

import static fi.iki.elonen.NanoHTTPD.IHTTPSession;
import static fi.iki.elonen.NanoHTTPD.Method;
import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.ResponseException;
import static fi.iki.elonen.router.RouterNanoHTTPD.UriResource;
import static fi.iki.elonen.router.RouterNanoHTTPD.UriResponder;
import static pl.pecet.nano_android_server.helper.Utils.forbidden;

@NoArgsConstructor
public class WrapperServerHandler implements UriResponder {

    private final Map<Method, SingleHandler> handler = new EnumMap<>(Method.class);
    //<editor-fold desc="fields">
    protected String TAG;

    {
        TAG = getClass().getSimpleName();
    }
    //</editor-fold>

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

    public WrapperServerHandler withHandler(@NonNull Method method, @NonNull SingleHandler handler) {
        this.handler.put(method, handler);
        return this;
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
    protected Response process(Method method, SingleData data) {
        return handler.containsKey(method)
                ? handler.getOrDefault(method, new SingleHandler()).prosess(data)
                : forbidden();
    }

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return process(session.getMethod(), SingleData
                .builder()
                .uriResource(uriResource)
                .urlParams(urlParams)
                .session(session)
                .build());
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return process(session.getMethod(), SingleData
                .builder()
                .uriResource(uriResource)
                .urlParams(urlParams)
                .session(session)
                .build());
    }

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return process(session.getMethod(), SingleData
                .builder()
                .uriResource(uriResource)
                .urlParams(urlParams)
                .session(session)
                .build());
    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return process(session.getMethod(), SingleData
                .builder()
                .uriResource(uriResource)
                .urlParams(urlParams)
                .session(session)
                .build());
    }

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return process(session.getMethod(), SingleData
                .builder()
                .uriResource(uriResource)
                .urlParams(urlParams)
                .session(session)
                .build());
    }
    //</editor-fold>

    @Getter
    @Builder
    public static class SingleData {

        private final UriResource uriResource;
        private final Map<String, String> urlParams;
        private final IHTTPSession session;
        private JsonElement jsonBody;
        private Map<String, String> formBody;

        public Map<String, List<String>> getQueryParams() {
            return session.getParameters();
        }

        public Map<String, String> getHeaders() {
            return session.getHeaders();
        }

        public Method getMethod() {
            return session.getMethod();
        }

        public JsonElement getJsonBody() throws IOException, ResponseException {
            if (jsonBody == null) {
                Map<String, String> formBody = getFormBody();

                String data = null;
                if (formBody != null) {
                    data = formBody.get("postData");
                }
                if (data == null) {
                    throw new IOException("postData == null");
                }

                jsonBody = new Gson().fromJson(data, JsonElement.class);
            }

            return jsonBody;
        }

        public Map<String, String> getFormBody() throws IOException, ResponseException {
            if (formBody == null) {
                formBody = new HashMap<>();
                session.parseBody(formBody);
            }
            return formBody;
        }
    }
}
