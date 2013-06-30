package org.easy.cdi.extension.eager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tages the bean to be eagerly created on container startup
 * like @Startup annotation in JEE but for CDI
 * http://ovaraksin.blogspot.de/2013/02/eager-cdi-beans.html
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Eager {
    
}
