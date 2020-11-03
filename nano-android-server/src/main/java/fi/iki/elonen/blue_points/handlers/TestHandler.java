package fi.iki.elonen.blue_points.handlers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.blue_points.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("rawtypes")
public class TestHandler extends TextHandler {

    protected TestHandler(@NonNull String mimeType, @NonNull NanoHTTPD.Response.IStatus status,
                          @NonNull Function<Request, NanoHTTPD.Response> process, @NonNull List<Object> parameters) {
        super(mimeType, status, process, parameters);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder> extends TextHandler.Builder<T> {

        @NonNull
        private String process(Request request) {
            StringBuilder text = new StringBuilder("<html><body>");
            text.append("<h1>Url: ");
            text.append(request.getSession().getUri());
            text.append("</h1><br>");
            Map<String, List<String>> queryParams = request.getQueryParams();
            if (queryParams.size() > 0) {
                for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    text.append("<p>Param '").append(key).append("' = ").append(value).append("</p>");
                }
            } else {
                text.append("<p>no params in url</p><br>");
            }
            return text.toString();
        }

        @Override
        protected void configure() {
            super.configure();
            withTextProcess(this::process);
        }
    }
}
