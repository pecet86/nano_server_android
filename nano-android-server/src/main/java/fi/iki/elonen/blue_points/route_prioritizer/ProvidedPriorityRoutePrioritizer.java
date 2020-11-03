package fi.iki.elonen.blue_points.route_prioritizer;

import java.util.Collection;
import java.util.PriorityQueue;

import fi.iki.elonen.blue_points.BluePoint;

public class ProvidedPriorityRoutePrioritizer extends BaseRoutePrioritizer {

    @Override
    public void addRoute(BluePoint resource) {
        addToMap(resource);
    }

    @Override
    protected Collection<BluePoint> newMappingCollection() {
        return new PriorityQueue<>();
    }

}
