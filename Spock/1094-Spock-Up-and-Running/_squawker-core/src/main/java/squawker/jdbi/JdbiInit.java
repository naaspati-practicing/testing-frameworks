package squawker.jdbi;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.ColumnMappers;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.mapper.RowMappers;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import squawker.Message;
import squawker.User;

public class JdbiInit {
	public static Jdbi init(String url, Logger sql_logger) {
		Jdbi jdbi = Jdbi.create(url);	
		jdbi.installPlugin(new SqlObjectPlugin());

		if(sql_logger != null)
			jdbi.setSqlLogger(new JdbiLogger(sql_logger));

		return jdbi;
	}
	private static class JdbiLogger implements SqlLogger {
		final Logger logger;
		public JdbiLogger(Logger logger) {
			this.logger = logger;
		}
		@Override
		public void logAfterExecution(StatementContext context) {
			logger.fine(context.getParsedSql().getSql());
		}
		@Override
		public void logException(StatementContext context, SQLException ex) {
			logger.log(Level.SEVERE, context.getParsedSql().getSql(), ex);
		}
	}

	public static void registerPersistentUserMapper(Jdbi jdbi, Supplier<FollowingStore> fs, Supplier<MessageStore> ms) {
		RowMapper<? extends User> mapper = new UserStore.PersistentUserMapper(fs, ms);
		jdbi.registerRowMapper(User.class, mapper);
		jdbi.registerRowMapper(PersistentUser.class, mapper);
	}
	public static void registerUserMapper(Jdbi jdbi) {
		ColumnMapper<Instant> colMap = jdbi.getConfig(ColumnMappers.class).findFor(Instant.class).get();
		jdbi.registerRowMapper(User.class, (rs, ctx) -> new User(Integer.valueOf(rs.getInt("id")), rs.getString("username"), colMap.map(rs, "registered", ctx)));
	}
	public static void registerMessageMapper(Jdbi jdbi, Supplier<UserStore> us, Supplier<PersistentUserCache> cache) {
		jdbi.registerRowMapper(Message.class, new MessageStore.MessageMapper(us, cache));
	}
	public static <E> ColumnMapper<E> columnMapper(Handle handle, Class<E> cls) {
		return handle.getConfig(ColumnMappers.class).findFor(cls).orElseThrow(() -> new IllegalMonitorStateException("no columnMapper found for: "+cls));
	}
}
