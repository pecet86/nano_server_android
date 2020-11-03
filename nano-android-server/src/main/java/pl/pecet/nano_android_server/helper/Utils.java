package pl.pecet.nano_android_server.helper;

import lombok.experimental.UtilityClass;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.Response.Status;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

@UtilityClass
public class Utils {

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static Response forbidden() {
        return newFixedLengthResponse(Status.FORBIDDEN, "text/html", "forbidden");
    }
}
