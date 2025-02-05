package org.springframework.beans.factory.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PreDestroy {
    // для методов, которые надо вызвать при закрытии контейнера
}
