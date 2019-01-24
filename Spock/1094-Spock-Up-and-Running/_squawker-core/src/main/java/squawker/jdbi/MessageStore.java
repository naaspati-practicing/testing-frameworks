package squawker.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.Message;
import squawker.User;

public interface MessageStore extends SqlObject {

	@UseClasspathSqlLocator
	@SqlUpdate
	void createTable();

	static class MessageMapper implements RowMapper<Message> {
		private final UserCache cache = UserCache.getInstance(); 

		@Override
		public Message map(ResultSet rs, StatementContext ctx) throws SQLException {
			int mid = rs.getInt("mid");
			User user = cache.get(mid);
			if(user == null) { 
				user = new User(mid, rs.getString("username"), Instant.ofEpochMilli(rs.getTimestamp("registered").getTime()));
				cache.put(user);
			}
			return new Message(user, rs.getString("text"), Instant.ofEpochMilli(rs.getTimestamp("posted_at").getTime()));
		}
	}

	@RegisterRowMapper(MessageMapper.class)
	@SqlQuery("SELECT m.*, u.*, m.id as mid, u.id as uid FROM Message LEFT JOIN ON m.posted_by = u.id WHERE m.id=?; ")
	Message find(@Bind int message_id) ;

	@SqlUpdate("delete from messages where id=?")
	void delete(@Bind int message_id);

	default Message insert(User user, String text, Instant postedAt) {
		Integer key = withHandle(h -> 
		h.createUpdate("INSERT INTO messages(posted_by, text, posted_at) SELECT id, :text, :at FROM users WHERE username=:username;")
		.bind("username", user.getUsername())
		.bind("text", text)
		.bind("at", postedAt)
		.executeAndReturnGeneratedKeys()
		.mapTo(Integer.class)
		.findOnly()
				);

		return new Message(key, user, text, postedAt);
	}
	default List<Message> postsBy(User user) {
		return withHandle(h -> 
			h.createQuery("SELECT * FROM messages WHERE posted_by=(SELECT id FROM users where username=?)")
			.bind(0, user.getUsername())
			.map((rs, ctx) -> new Message(rs.getInt("id"), user, rs.getString("text"), Instant.ofEpochMilli(rs.getTimestamp("posted_at").getTime())))
			.list()
		);
	}
	
	default List<Message> postsBy(String username) {
		User user = withHandle(h ->
		  h.createQuery("SELECT * FROM users WHERE username=?")
		  .bind(0, username)
		  .map(ConstructorMapper.of(User.class))
		  .findOnly()
		);
		return postsBy(user);
	}
	
	@UseClasspathSqlLocator
	@SqlQuery
	@RegisterRowMapper(MessageMapper.class)
	List<Message> timeline(@BindBean("u") User user);
}
