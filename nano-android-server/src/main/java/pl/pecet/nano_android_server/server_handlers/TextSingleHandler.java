package pl.pecet.nano_android_server.server_handlers;

import androidx.annotation.NonNull;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static pl.pecet.nano_android_server.server_handlers.WrapperServerHandler.SingleData;

public abstract class TextSingleHandler extends SingleHandler {

    @NonNull
    protected abstract String getText(SingleData data);

    @NonNull
    @Override
    protected String getMimeType() {
        return "text/html";
    }

    @NonNull
    @Override
    public Response prosess(SingleData data) {
        return newFixedLengthResponse(
                getStatus(),
                getMimeType(),
                getText(data)
        );
    }
}
