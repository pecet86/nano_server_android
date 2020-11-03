package fi.iki.elonen.blue_points.route_prioritizer;

import java.util.ArrayList;
import java.util.Collection;

import fi.iki.elonen.blue_points.BluePoint;

public class InsertionOrderRoutePrioritizer extends BaseRoutePrioritizer {

    @Override
    protected Collection<BluePoint> newMappingCollection() {
        return new ArrayList<>();
    }
}
