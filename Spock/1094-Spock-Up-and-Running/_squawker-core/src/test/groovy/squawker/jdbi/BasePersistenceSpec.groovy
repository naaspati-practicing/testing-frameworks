package squawker.jdbi

import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.SqlLogger
import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.sqlobject.SqlObjectPlugin

import spock.lang.Shared
import spock.lang.Specification
import squawker.User

abstract class BasePersistenceSpec extends Specification {
	private final static Logger logger = Logger.getLogger(BasePersistenceSpec.class.simpleName)

	@Shared Jdbi jdbi
	@Shared Handle handle
	@Shared UserStore userStore;

	def setupSpec() {
		assert jdbi == null
		jdbi = init()
		handle = jdbi.open()
		userStore = handle.attach(UserStore)
		userStore.createTable()

		logger.fine "jdbi init"
	}
	
	private Jdbi init() {
		Logger logger = Logger.getLogger("SQL");
		Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
		jdbi.installPlugin(new SqlObjectPlugin());

		jdbi.sqlLogger = new SqlLogger() {
			@Override
			public void logAfterExecution(StatementContext context) {
				logger.fine(context.parsedSql.sql);
			}
			@Override
			public void logException(StatementContext context, SQLException ex) {
				logger.log(Level.SEVERE, context.parsedSql.sql, ex);
			}
		}
		return jdbi;
	}
	
	def cleanup() {
		if(handle != null) 
			handle.execute("delete from users")
	}
	def cleanupSpec() {
		if(handle != null) {
			handle.execute("drop table user if exists")
			handle.close()
		}
	}
	
	def rowCount(tablename) {
		handle.createQuery("select count(*) from $tablename")
		.mapTo(Integer)
		.findOnly()
	}
	
	User kirk() { new User("kirk")}
	User spark() { new User("spark")}
}
