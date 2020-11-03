package fi.iki.elonen.blue_points.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fi.iki.elonen.blue_points.route_prioritizer.DefaultRoutePrioritizer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BluePointHost {
    /**
     * @return host
     */
    String host();

    /**
     * @return port
     */
    int port() default 8080;

    /**
     * @return prioritizer
     */
    Class<?> prioritizer() default DefaultRoutePrioritizer.class;
}
