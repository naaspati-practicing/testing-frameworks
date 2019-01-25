package squawker.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.Message;
import squawker.User;

public interface MessageStore extends SqlObject {
	String SELECT = "SELECT * FROM messages";
	String DELETE = "DELETE FROM messages";

	@UseClasspathSqlLocator
	@SqlUpdate
	void createTable();

	class MessageMapper implements RowMapper<Message> {
		private final Supplier<UserStore> us;
		private ColumnMapper<Instant> instantMapper;
		final Supplier<PersistentUserCache> cache;

		public MessageMapper(Supplier<UserStore> us, Supplier<PersistentUserCache> cache) {
			this.us = us;
			this.cache = cache;
		}

		@Override
		public Message map(ResultSet rs, StatementContext ctx) throws SQLException {
			UserStore us = this.us.get();
			PersistentUserCache cache = this.cache.get();
			
			if(instantMapper == null)
				instantMapper = JdbiInit.columnMapper(us.getHandle(), Instant.class); 
			
			int posted_by = rs.getInt("posted_by");
			PersistentUser user = us.find(posted_by, cache);

			return new Message(user, rs.getString("text"), instantMapper.map(rs, "posted_at", ctx));
		}
	}

	@SqlQuery
	@UseJoinSqlAnnotationLocator
	@JoinSql({SELECT, "where id=?"})
	Message find(@Bind int message_id) ;

	@SqlUpdate
	@UseJoinSqlAnnotationLocator
	@JoinSql({DELETE, "where id=?"})
	void delete(@Bind int message_id);

	default Message insert(User user, String text, Instant postedAt) {
		Integer key = withHandle(h -> {
			return h.createUpdate("INSERT INTO messages(posted_by, text, posted_at) select id, :text,:posted_at FROM users where username=:username")
					.bind("username", user.getUsername())
					.bind("text", text)
					.bind("posted_at", postedAt)
					.executeAndReturnGeneratedKeys()
					.mapTo(Integer.class)
					.findOnly();
		});
		return new Message(key, user, text, postedAt);
	}

	default List<Message> postsBy(User user) {
		if(user instanceof PersistentUser)
			return postsBy((int)user.getId());
		else
			return postsBy(user.getUsername());
	}
	
	@SqlQuery
	@UseJoinSqlAnnotationLocator
	@JoinSql({SELECT, "where posted_by=?"})
	List<Message> postsBy(@Bind int user_id);
	@SqlQuery
	@UseJoinSqlAnnotationLocator
	@JoinSql({SELECT, "where posted_by=(select id from users where username=?)"})
	List<Message> postsBy(@Bind String username);

	default Message latestPostBy(User user) {
		if(user instanceof PersistentUser)
			return latestPostBy((int)user.getId());
		else
			return latestPostBy(user.getUsername());
	}
	
	@SqlQuery
	@UseJoinSqlAnnotationLocator
	@JoinSql({SELECT, "where posted_by=?", "order by posted_at desc limit 1"})
	Message latestPostBy(@Bind int user_id);
	@SqlQuery
	@UseJoinSqlAnnotationLocator
	@JoinSql({SELECT, "where posted_by=(select id from users where username=?)", "order by posted_at desc limit 1"})
	Message latestPostBy(@Bind String username);
 

	@UseClasspathSqlLocator
	@SqlQuery
	List<Message> timeline(@BindBean User user);
}
