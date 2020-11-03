package pl.pecet.nano_server_android.backend;

import fi.iki.elonen.blue_points.Request;
import fi.iki.elonen.blue_points.annotations.BluePointHost;
import fi.iki.elonen.blue_points.annotations.BluePointRoute;
import fi.iki.elonen.blue_points.handlers.JsonHandler;

import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;
import static fi.iki.elonen.NanoHTTPD.Response;

@BluePointHost(host = "localhost")
public class Api {

    @BluePointRoute(path = "/test_get/:name/file", method = GET)
    public Response testGet(Request request) {
        return JsonHandler
                .builder()
                .withJsonProcess(ApiService::testGetExecut)
                .build()
                .process(request);
    }

    @BluePointRoute(path = "/test_post/:name/file", method = POST)
    public Response testPost(Request request) {
        return JsonHandler
                .builder()
                .withJsonProcess(ApiService::testPostExecut)
                .build()
                .process(request);
    }
}
