package fi.iki.elonen.blue_points.route_prioritizer;

import android.util.Log;

import java.util.Collection;

import fi.iki.elonen.blue_points.BluePoint;
import fi.iki.elonen.blue_points.Utils;

import static java.util.Collections.unmodifiableCollection;

public abstract class BaseRoutePrioritizer implements RoutePrioritizer {

    protected final Collection<BluePoint> mappings;
    protected String TAG;

    {
        TAG = getClass().getSimpleName();
    }

    public BaseRoutePrioritizer() {
        mappings = newMappingCollection();
    }

    @Override
    public void addRoute(BluePoint resource) {
        resource.setPriority(resource.getPriority() + mappings.size());
        addToMap(resource);
    }

    //<editor-fold desc="base">
    protected void addToMap(BluePoint route) {
        if (route != null) {
            mappings.add(route);
        }
    }
    //</editor-fold>

    @Override
    public Collection<BluePoint> getPrioritizedRoutes() {
        return unmodifiableCollection(mappings);
    }

    protected abstract Collection<BluePoint> newMappingCollection();

    //<editor-fold desc="logs">
    protected void log(String path, String value) {
        Utils.log(TAG, path, value, Log.DEBUG);
    }

    protected void log(String path, String value, int priority) {
        Utils.log(TAG, path, value, priority);
    }

    protected void log(String path, String value, Throwable th) {
        Utils.log(TAG, path, value, th);
    }
    //</editor-fold>
}
