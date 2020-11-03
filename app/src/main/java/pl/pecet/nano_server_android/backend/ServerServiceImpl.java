package pl.pecet.nano_server_android.backend;

import androidx.annotation.NonNull;

import fi.iki.elonen.blue_points.RouterNanoHTTPD;
import pl.pecet.nano_android_server.ServerService;

public class ServerServiceImpl extends ServerService {
    @NonNull
    @Override
    protected RouterNanoHTTPD createServer() {
        return RouterNanoHTTPD.create(Api.class);
    }
}
