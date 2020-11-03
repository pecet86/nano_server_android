package fi.iki.elonen.blue_points.route_prioritizer;

import java.util.Collection;

import fi.iki.elonen.blue_points.BluePoint;

public interface RoutePrioritizer {

    void addRoute(BluePoint resource);

    Collection<BluePoint> getPrioritizedRoutes();
}
