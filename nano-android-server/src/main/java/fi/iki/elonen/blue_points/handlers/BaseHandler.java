package fi.iki.elonen.blue_points.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.blue_points.Request;
import fi.iki.elonen.blue_points.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.Response.IStatus;
import static fi.iki.elonen.NanoHTTPD.newChunkedResponse;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static fi.iki.elonen.blue_points.Utils.CONTENT_TYPE_HTML;
import static fi.iki.elonen.blue_points.Utils.CONTENT_TYPE_JSON;
import static fi.iki.elonen.blue_points.Utils.forbiddenHandler;

@SuppressWarnings("rawtypes")
public class BaseHandler {

    @NonNull
    @Getter(value = AccessLevel.PROTECTED)
    private final String mimeType;
    @NonNull
    @Getter(value = AccessLevel.PROTECTED)
    private final IStatus status;
    @NonNull
    private final Function<Request, Response> process;
    @NonNull
    private final List<Object> parameters;
    //<editor-fold desc="fields">
    protected String TAG;
    //</editor-fold>

    {
        TAG = getClass().getSimpleName();
    }

    protected BaseHandler(@NonNull String mimeType, @NonNull IStatus status,
                          @NonNull Function<Request, Response> process, @NonNull List<Object> parameters) {
        this.mimeType = mimeType;
        this.status = status;
        this.process = process;
        this.parameters = parameters;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NonNull
    public Response process(Request request) {
        return process.apply(request);
    }

    @SuppressWarnings("unchecked")
    public <V> V getParamAs(int index) {
        return (V) getParam(index);
    }

    @SuppressWarnings("unchecked")
    public <V> V getParamAs(Class<V> type, int index) {
        return (V) getParam(index);
    }
    //</editor-fold>

    //<editor-fold desc="logs">
    protected void log(String path, String value) {
        Utils.log(TAG, path, value, Log.DEBUG);
    }

    //<editor-fold desc="getParam">
    @SuppressWarnings("unchecked")
    public <V> V getParam(int index) {
        if (parameters != null) {
            log("getParam", "init parameter is null");
            return null;
        }
        if (index < 0 || index >= parameters.size()) {
            log("getParam", "init parameter index not available " + index);
            return null;
        }
        return (V) parameters.get(index);
    }

    protected void log(String path, String value, int priority) {
        Utils.log(TAG, path, value, priority);
    }
    //</editor-fold>

    protected void log(String path, String value, Throwable th) {
        Utils.log(TAG, path, value, th);
    }

    @FunctionalInterface
    public interface FunctionResponse extends Function<Request, Response> {
    }

    @FunctionalInterface
    public interface FunctionString extends Function<Request, String> {
    }

    @FunctionalInterface
    public interface FunctionJsonElement extends Function<Request, JsonElement> {
    }

    @FunctionalInterface
    public interface FunctionInputStream extends Function<Request, InputStream> {
    }

    @SuppressWarnings("unchecked")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder> {
        private final List<Object> parameters = new LinkedList<>();
        protected String TAG;
        @Getter
        private String mimeType;
        @Getter
        private IStatus status;
        private Function<Request, Response> process;
        private Function<Request, String> processText;
        private Function<Request, InputStream> processInputStream;

        {
            TAG = getClass().getSimpleName();
        }

        public T withMimeType(@NonNull String mimeType) {
            this.mimeType = mimeType;
            return (T) this;
        }

        public T withStatus(@NonNull IStatus status) {
            this.status = status;
            return (T) this;
        }

        protected T withProcess(@NonNull FunctionResponse process) {
            this.process = process;
            return (T) this;
        }

        protected T withTextProcess(@NonNull FunctionString process) {
            mimeType = CONTENT_TYPE_HTML;
            processText = process;
            return (T) this;
        }

        protected T withJsonProcess(@NonNull FunctionJsonElement process) {
            mimeType = CONTENT_TYPE_JSON;
            processText = (r) -> process.apply(r).toString();
            return (T) this;
        }

        protected T withInputStreamProcess(@NonNull FunctionInputStream process) {
            processInputStream = process;
            return (T) this;
        }

        protected T addParam(Object element) {
            parameters.add(element);
            return (T) this;
        }

        protected void configure() {
            if (status == null) {
                status = Status.OK;
            }
            if (mimeType == null) {
                mimeType = CONTENT_TYPE_HTML;
            }
        }

        private Function<Request, Response> getProcess() {
            if (processText != null) {
                return request -> newFixedLengthResponse(
                        getStatus(),
                        getMimeType(),
                        processText.apply(request)
                );
            } else if (processInputStream != null) {
                return request -> newChunkedResponse(
                        getStatus(),
                        getMimeType(),
                        processInputStream.apply(request)
                );
            } else if (process != null) {
                return process;
            } else {
                return request -> forbiddenHandler();
            }
        }

        public BaseHandler build() {
            configure();
            return new BaseHandler(mimeType, status, getProcess(), parameters);
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
}
