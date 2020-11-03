package pl.pecet.nano_server_android.handles;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.iki.elonen.NanoHTTPD;
import pl.pecet.nano_android_server.server_handlers.JsonSingleHandler;
import pl.pecet.nano_android_server.server_handlers.WrapperServerHandler;

public class TestGetServerHandler extends WrapperServerHandler {

    public TestGetServerHandler() {
        withHandler(NanoHTTPD.Method.GET, new JsonSingleHandler() {
            @NonNull
            @Override
            protected JsonElement getJson(SingleData data) {
                try {
                    log("getJson", data.getQueryParams().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObject body = new JsonObject();
                body.addProperty("XXX", "TestGetServerHandler");
                return body;
            }
        });
    }
}
