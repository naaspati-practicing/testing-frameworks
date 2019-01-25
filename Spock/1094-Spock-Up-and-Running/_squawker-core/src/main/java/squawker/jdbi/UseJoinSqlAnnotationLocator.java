package squawker.jdbi;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jdbi.v3.sqlobject.config.ConfiguringAnnotation;
import org.jdbi.v3.sqlobject.locator.AnnotationSqlLocator;

@ConfiguringAnnotation(UseJoinSqlAnnotationLocatorImpl.class)
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface UseJoinSqlAnnotationLocator { }




