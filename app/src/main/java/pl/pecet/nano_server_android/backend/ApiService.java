package pl.pecet.nano_server_android.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.iki.elonen.blue_points.Request;
import fi.iki.elonen.blue_points.Utils;

public class ApiService {

    private static final String TAG = ApiService.class.getSimpleName();

    public static JsonElement testGetExecut(Request request) {
        Utils.log(TAG, "testGetExecut", "ok");
        try {
            Utils.log(TAG, "getJson", request.getQueryParams().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject body = new JsonObject();
        body.addProperty("XXX", "TestGetServerHandler");
        return body;
    }

    public static JsonElement testPostExecut(Request request) {
        try {
            Utils.log(TAG, "getJson", request.getQueryParams().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Utils.log(TAG, "getJson", request.getJsonBody().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject body = new JsonObject();
        body.addProperty("XXX", "TestPostServerHandler");
        return body;
    }
}
