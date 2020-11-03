package fi.iki.elonen.blue_points;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.ResponseException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Request {
    private final BluePoint bluePoint;
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
