package fi.iki.elonen.blue_points;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.blue_points.annotations.BluePointError404;
import fi.iki.elonen.blue_points.annotations.BluePointHost;
import fi.iki.elonen.blue_points.annotations.BluePointRoute;
import fi.iki.elonen.blue_points.route_prioritizer.RoutePrioritizer;

public class RouterNanoHTTPD extends NanoHTTPD {

    private static final String TAG = RouterNanoHTTPD.class.getSimpleName();

    private final UriRouter router;
    private final Class<?> handler;
    private WeakReference<Object> proxy;

    private RouterNanoHTTPD(String hostname, int port, Class<?> prioritizer, Class<?> handler) {
        super(hostname, port);
        this.handler = handler;
        try {
            router = new UriRouter((RoutePrioritizer) prioritizer.newInstance());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }


        configure();
    }

    public static RouterNanoHTTPD create(@NonNull Class<?> handler) {
        if (handler.isAnnotationPresent(BluePointHost.class)) {
            BluePointHost annotation = handler.getAnnotation(BluePointHost.class);
            assert annotation != null;
            return new RouterNanoHTTPD(annotation.host(), annotation.port(), annotation.prioritizer(), handler);
        } else {
            throw new IllegalArgumentException("klasa nie zawiera @BluePointHost");
        }
    }

    //<editor-fold desc="configure">
    protected void configure() {
        try {
            router.setError404(new BluePoint(this, Utils.class.getMethod("error404Handler")));
        } catch (NoSuchMethodException ignore) {
        }
        addMappings();
    }

    protected void addMappings() {
        for (java.lang.reflect.Method method : handler.getMethods()) {
            if (method.isAnnotationPresent(BluePointRoute.class)) {
                addBluePointRoute(method);
            } else if (method.isAnnotationPresent(BluePointError404.class)) {
                addBluePointError404(method);
            }
        }
    }

    protected void addBluePointRoute(@NonNull java.lang.reflect.Method handler) {
        BluePointRoute annotation = handler.getAnnotation(BluePointRoute.class);

        assert annotation != null;
        router.addRoute(new BluePoint(this, annotation, handler));
    }

    protected void addBluePointError404(@NonNull java.lang.reflect.Method handler) {
        BluePointError404 annotation = handler.getAnnotation(BluePointError404.class);

        assert annotation != null;
        router.setError404(new BluePoint(this, handler));
    }
    //</editor-fold>

    protected synchronized Object getProxy() throws Exception {
        Object proxy = null;
        if (this.proxy != null) {
            proxy = this.proxy.get();
        }

        if (proxy == null) {
            proxy = handler.newInstance();
            this.proxy = new WeakReference<>(proxy);
        }
        return proxy;
    }

    @Override
    public Response serve(IHTTPSession session) {
        //Utils.log(TAG, "serve", "ok");
        return router.process(session);
    }
}

