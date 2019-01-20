package squawker.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import squawker.Message;
import squawker.User;

public interface DataStore {
	
	String CREATE_USER_TABLE = 
			"CREATE TABLE USERS (" + 
					"  id INTEGER PRIMARY KEY AUTO_INCREMENT," + 
					"  username VARCHAR(32) UNIQUE," + 
					"  registered TIMESTAMP)";
	
	String CREATE_FOLLOWING_TABLE = 
			"CREATE TABLE  FOLLOWINGS  (" +
			"  follower INTEGER NOT NULL," +
			"  followee INTEGER NOT NULL" + 
			")";
	
	String CREATE_MESSAGE_TABLE = 
			"CREATE TABLE MESSAGES (\n" + 
			"  id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" + 
			"  posted_by INTEGER NOT NULL," + 
			"  _text VARCHAR(140) NOT NULL, " + 
			"  posted_at TIMESTAMP NOT NULL" + 
			")";


	@SqlUpdate(CREATE_USER_TABLE)
	void createUserTable() ;
	@SqlUpdate(CREATE_FOLLOWING_TABLE)
	void createFollowingTable() ;
	@SqlUpdate(CREATE_MESSAGE_TABLE)
	void createMessageTable() ;
	
	@SqlUpdate("INSERT INTO USERS(username, registered) VALUES(:username, :registered)")
	void insert(@BindBean User user);
	
	@SqlQuery("SELECT * FROM USERS")
	@RegisterConstructorMapper(User.class)
	List<User> allUsers() ;
	
	@SqlQuery("SELECT * FROM USERS WHERE username = ?")
	@RegisterConstructorMapper(User.class)
	User getUserByUsername(@Bind String username) ;
	
	@SqlUpdate("INSERT INTO FOLLOWINGS(follower, followee)  " + 
			"SELECT er.id, ee.id FROM USERS er, USERS ee WHERE er.username = :er.username AND ee.username = :ee.username;")
	void follow(@BindBean("er") User follower, @BindBean("ee") User followee); 

	@SqlQuery("SELECT * FROM USERS WHERE id IN " + 
			"(SELECT followee FROM followings where follower = (select id from users where username=:username))")
	@RegisterConstructorMapper(User.class)
	List<User> findFollowees(@BindBean User user);
	
	@SqlUpdate("INSERT INTO MESSAGES (posted_by, _text, posted_at) SELECT u.id, :text, :postedAt FROM USERS u where u.username = :postedBy.username")
	void insert(@BindBean Message message);
	
	public static class MessageMapper implements RowMapper<Message> {
		private final HashMap<String, User> users = new HashMap<>();

		@Override
		public Message map(ResultSet rs, StatementContext ctx) throws SQLException {
			String username = rs.getString("username");
			User user = users.get(username);
			ColumnMapper<Instant> instant =  ctx.findColumnMapperFor(Instant.class).get();
			
			if(user == null) {
				user = new User(rs.getInt("uid"), username, instant.map(rs, "registered", ctx));
				users.put(username, user);
			}
			
			return new Message(
					rs.getInt("mid"), 
					user, rs.getString("_text"), 
					instant.map(rs, "posted_at", ctx));
		}
	}
	
	@RegisterRowMapper(MessageMapper.class)
	@SqlQuery("SELECT *, m.id as mid, u.id as uid FROM MESSAGES m LEFT JOIN USERS u ON m.posted_by = u.id  WHERE username = :username")
	List<Message> postsBy(@BindBean User user) ;
	
	@RegisterRowMapper(MessageMapper.class)
	@SqlQuery("SELECT *, m.id as mid, u.id as uid FROM MESSAGES m LEFT JOIN USERS u ON m.posted_by = u.id  "
			+ "WHERE username = :username OR "
			+ "uid IN(SELECT followee from FOLLOWING WHERE follower = (select id FROM USERS WHERE username = :username))")
	Collection<Message> timeline(User user);
	
	@SqlQuery("SELECT COUNT(*) FROM <tablename>")
	int rowCount(@Bind("tablename") String tablename) ;
}
