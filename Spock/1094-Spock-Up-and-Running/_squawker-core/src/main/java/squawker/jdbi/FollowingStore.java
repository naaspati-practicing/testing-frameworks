package squawker.jdbi;

import java.util.List;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.User;

@UseClasspathSqlLocator
public interface FollowingStore extends SqlObject {
	
	@SqlUpdate
	void createTable();
	
	@SqlUpdate
	void follow(
			@BindBean("u") User follower, 
			@BindBean("v") User followee 
			) ;
	
	@SqlQuery
	List<User> findFollowees(@BindBean User follower) ;
	
	@SqlQuery
	List<User> findFollowers(@BindBean User followee) ;

}
