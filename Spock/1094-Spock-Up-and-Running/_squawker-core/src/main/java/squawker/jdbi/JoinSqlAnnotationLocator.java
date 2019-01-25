package squawker.jdbi;

import java.lang.reflect.Method;

import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.sqlobject.locator.SqlLocator;

public class JoinSqlAnnotationLocator implements SqlLocator {
    @Override
    public String locate(Class<?> sqlObjectType, Method method, ConfigRegistry config) {
    	JoinSql sql = method.getAnnotation(JoinSql.class);
    	if(sql.value().length == 0)
    		throw new IllegalStateException("no values specified");
    	
        return String.join(" ", sql.value());
    }
}