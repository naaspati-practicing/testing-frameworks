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
	Handle handle
	DataStore datastore;

	def setupSpec() {
		assert jdbi == null
		jdbi = init()

		logger.fine "jdbi init"
	}
	
	private Jdbi init() {
		Logger logger = Logger.getLogger("SQL");
		Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
		jdbi.installPlugin(new SqlObjectPlugin());

		jdbi.sqlLogger = new SqlLogger() {
			@Override
			public void logAfterExecution(StatementContext context) {
				logger.fine(context.getParsedSql().toString());
			}
			@Override
			public void logException(StatementContext context, SQLException ex) {
				logger.log(Level.SEVERE, context.getParsedSql().toString(), ex);
			}
		}
		return jdbi;
	}

	def setup() {
		handle = jdbi.open()
		datastore = handle.attach(DataStore)
	}
	def cleanup() {
		if(handle != null) {
			// note:: H2 memory database disposes DB data on connection close
			
			handle.execute(
					"drop table if exists USERS;"+
					"drop table if exists MESSAGES;"+
					"drop table  if exists FOLLOWINGS;")
			handle.close()
		}
	}
	
	User kirk() { new User("kirk")}
	User spark() { new User("spark")}
}
