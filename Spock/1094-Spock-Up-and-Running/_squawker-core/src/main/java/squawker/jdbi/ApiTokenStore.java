package squawker.jdbi;

import java.util.UUID;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.User;


public interface ApiTokenStore extends SqlObject {

	@UseClasspathSqlLocator
	@SqlUpdate
	void createTable();
	
	default String generateTokenFor(User user) {
		if(user.getId().getClass() != Integer.class)
			throw new IllegalStateException("unpersisted user: "+user);
		
		String token = UUID.randomUUID().toString();
		
		useHandle(h -> h.createUpdate("INSERT INTO api_token (token, user_id) SELECT :token, u.id FROM users u WHERE u.username = :username;")
				.bind("token", token)
				.bind("username", user.getUsername())
				.execute());
		
		return token;
	} 
	
	@SqlQuery("SELECT u.* FROM users u, api_token t WHERE t.token = :token AND t.user_id = u.id")
	@RegisterConstructorMapper(prefix = "u", value=User.class)
	public User find(@Bind("token") String token) ;
	
	@SqlQuery("SELECT t.token FROM users u, api_token t WHERE u.username = :username AND t.user_id = u.id")
	public String getToken(@BindBean("u") User u) ;
	
}
