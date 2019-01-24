package squawker.jdbi;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.User;

@RegisterConstructorMapper(User.class)
public interface UserStore extends SqlObject {

	@UseClasspathSqlLocator
	@SqlUpdate
	void createTable() ;

	default User insert(String username, Instant at) {
		return withHandle(h -> {
			int n = h.createUpdate("INSERT INTO users(username, registered) VALUES(?,?);")
					.bind(0, username)
					.bind(1, at)
					.executeAndReturnGeneratedKeys()
					.mapTo(Integer.class)
					.findOnly();
			return new User(n, username, at);
		});
	}

	default User insert(String username) {
		return insert(username, Instant.now());
	}

	@SqlQuery("SELECT * FROM users WHERE username=?;")
	User find(@Bind String username);

	@SqlQuery("SELECT * FROM users WHERE id=?;")
	User find(@Bind int username);

	@SqlQuery("SELECT * FROM users;")
	List<User> findAll();

	@SqlQuery("SELECT id FROM users WHERE upper(username)=upper(?);" )
	OptionalInt usernameInUse(@Bind String username); 
}
