package squawker.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.mapper.RowMappers;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.User;

public interface UserStore extends SqlObject {

	@UseClasspathSqlLocator
	@SqlUpdate
	void createTable() ;

	class PersistentUserMapper implements RowMapper<PersistentUser> {
		final Supplier<FollowingStore> fs;
		final Supplier<MessageStore> ms;

		public PersistentUserMapper(Supplier<FollowingStore> fs, Supplier<MessageStore> ms) {
			this.fs = fs;
			this.ms = ms;
		}

		@Override
		public PersistentUser map(ResultSet rs, StatementContext ctx) throws SQLException {
			return new PersistentUser(
					fs.get(),
					ms.get(), 
					rs.getInt("id"), 
					rs.getString("username"), 
					ctx.findColumnMapperFor(Instant.class).get().map(rs, "registered", ctx));
		}

	}

	default PersistentUser find(int user_id, PersistentUserCache cache) {
		PersistentUser u = cache == null ? null : cache.get(user_id);

		if(u != null)
			return u;

		u = withHandle(h -> {
			return h.createQuery("SELECT * FROM users WHERE id="+user_id)
					.mapTo(PersistentUser.class)
					.findOnly();
		});

		if(cache != null)
			cache.put(u);
		return u;
	}

	default User insert(String username, Instant at) {
		return withHandle(h -> {
			int id = h.createUpdate("INSERT INTO users(username, registered) VALUES(?,?);")
					.bind(0, username)
					.bind(1, at)
					.executeAndReturnGeneratedKeys()
					.mapTo(Integer.class)
					.findOnly();
			
			PersistentUserMapper pur = (PersistentUserMapper)h.getConfig(RowMappers.class).findFor(PersistentUser.class).get();
			return new PersistentUser(pur.fs.get(), pur.ms.get(), id, username, at);
		});
	}

	default User insert(String username) {
		return insert(username, Instant.now());
	}

	@SqlQuery("SELECT * FROM users WHERE username=?;")
	User find(@Bind String username);

	@SqlQuery("SELECT * FROM users;")
	List<User> findAll();

	@SqlQuery("SELECT id FROM users WHERE upper(username)=upper(?);" )
	OptionalInt usernameInUse(@Bind String username);
}
