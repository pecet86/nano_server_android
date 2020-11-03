package pl.pecet.nano_server_android.handles;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.iki.elonen.NanoHTTPD;
import pl.pecet.nano_android_server.server_handlers.JsonSingleHandler;
import pl.pecet.nano_android_server.server_handlers.WrapperServerHandler;

public class TestPostServerHandler extends WrapperServerHandler {

    public TestPostServerHandler() {
        withHandler(NanoHTTPD.Method.POST, new JsonSingleHandler() {
            @NonNull
            @Override
            protected JsonElement getJson(SingleData data) {
                try {
                    log("getJson", data.getQueryParams().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    log("getJson", data.getJsonBody().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObject body = new JsonObject();
                body.addProperty("XXX", "TestPostServerHandler");
                return body;
            }
        });
    }
}
