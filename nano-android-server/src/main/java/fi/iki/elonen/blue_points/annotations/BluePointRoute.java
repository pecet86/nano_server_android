package fi.iki.elonen.blue_points.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fi.iki.elonen.NanoHTTPD.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BluePointRoute {
    /**
     * @return path
     */
    String path() default "";

    /**
     * @return metoda wywołania
     */
    Method method() default Method.GET;

    /**
     * @return priority
     */
    int priority() default -1;
}
