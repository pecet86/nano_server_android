package pl.pecet.nano_android_server.server_handlers;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import static pl.pecet.nano_android_server.helper.Utils.CONTENT_TYPE_JSON;
import static pl.pecet.nano_android_server.server_handlers.WrapperServerHandler.SingleData;

public abstract class JsonSingleHandler extends TextSingleHandler {

    @NonNull
    @Override
    protected String getText(SingleData data) {
        return getJson(data).toString();
    }

    @NonNull
    @Override
    protected String getMimeType() {
        return CONTENT_TYPE_JSON;
    }

    @NonNull
    protected abstract JsonElement getJson(SingleData data);

}
