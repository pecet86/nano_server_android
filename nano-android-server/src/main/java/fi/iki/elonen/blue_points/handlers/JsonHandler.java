package fi.iki.elonen.blue_points.handlers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Function;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.blue_points.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("rawtypes")
public class JsonHandler extends TextHandler {

    protected JsonHandler(@NonNull String mimeType, @NonNull IStatus status,
                          @NonNull Function<Request, Response> process, @NonNull List<Object> parameters) {
        super(mimeType, status, process, parameters);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder> extends TextHandler.Builder<T> {

        @Override
        public T withJsonProcess(@NonNull FunctionJsonElement process) {
            return super.withJsonProcess(process);
        }
    }
}
