package com.jean_eric_espiegle.bug_tracking_application.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggableAction {
    String action();

    String entity() default "";
}
