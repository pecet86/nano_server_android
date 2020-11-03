package fi.iki.elonen.blue_points;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.blue_points.route_prioritizer.RoutePrioritizer;
import lombok.Setter;

import static fi.iki.elonen.blue_points.Utils.normalizeUri;

public class UriRouter {

    private static final String TAG = UriRouter.class.getSimpleName();
    private final RoutePrioritizer prioritizer;
    @Setter
    private BluePoint error404;

    public UriRouter(RoutePrioritizer prioritizer) {
        this.prioritizer = prioritizer;
    }

    public Response process(IHTTPSession session) {
        //Utils.log(TAG, "process", "ok");
        String work = normalizeUri(session.getUri());
        for (BluePoint bluePoint : prioritizer.getPrioritizedRoutes()) {
            Map<String, String> params = bluePoint.match(work, session.getMethod());
            if (params != null) {
                //Utils.log(TAG, "process:bluePoint", "ok");
                return bluePoint.process(params, session);
            }
        }
        //Utils.log(TAG, "process:error404", "ok");
        return error404.process(null, session);
    }

    void addRoute(BluePoint route) {
        prioritizer.addRoute(route);
    }

}
