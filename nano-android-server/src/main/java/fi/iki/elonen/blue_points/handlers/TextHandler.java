package fi.iki.elonen.blue_points.handlers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Function;

import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.blue_points.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static fi.iki.elonen.NanoHTTPD.Response;

@SuppressWarnings("rawtypes")
public class TextHandler extends BaseHandler {

    protected TextHandler(@NonNull String mimeType, @NonNull IStatus status,
                          @NonNull Function<Request, Response> process, @NonNull List<Object> parameters) {
        super(mimeType, status, process, parameters);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder> extends BaseHandler.Builder<T> {

        @Override
        public T withTextProcess(@NonNull FunctionString process) {
            return super.withTextProcess(process);
        }
    }
}
