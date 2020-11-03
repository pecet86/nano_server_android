package pl.pecet.nano_server_android;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import pl.pecet.nano_android_server.NanoAndroidServer;
import pl.pecet.nano_android_server.ServerService;
import pl.pecet.nano_server_android.handles.TestGetServerHandler;
import pl.pecet.nano_server_android.handles.TestPostServerHandler;

public class ServerServiceImpl extends ServerService {
    @NonNull
    @Override
    protected NanoAndroidServer createServer() {
        return new NanoAndroidServer() {
            @Override
            public List<Route<UriResponder>> getMappingsRoute() {
                List<Route<UriResponder>> routes = new LinkedList<>();

                routes.add(Route
                        .builder()
                        .url("/test_get/:name/file")
                        .handler(TestGetServerHandler.class)
                        .build());

                routes.add(Route
                        .builder()
                        .url("/test_post/:name/file")
                        .handler(TestPostServerHandler.class)
                        .build());

                return routes;
            }
        };
    }
}
