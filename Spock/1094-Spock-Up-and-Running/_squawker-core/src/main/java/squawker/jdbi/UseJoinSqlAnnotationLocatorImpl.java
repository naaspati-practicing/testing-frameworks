package squawker.jdbi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.sqlobject.SqlObjects;
import org.jdbi.v3.sqlobject.config.Configurer;

public class UseJoinSqlAnnotationLocatorImpl implements Configurer {
	
	@Override
	public void configureForType(ConfigRegistry registry, Annotation annotation, Class<?> sqlObjectType) {
		registry.get(SqlObjects.class).setSqlLocator(new JoinSqlAnnotationLocator());
	}

	@Override
	public void configureForMethod(ConfigRegistry registry, Annotation annotation, Class<?> sqlObjectType, Method method) {
		configureForType(registry, annotation, sqlObjectType);
	}
}