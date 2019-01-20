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
import spock.lang.Subject
import squawker.User

abstract class BasePersistenceSpec extends Specification {
	private final static Logger logger = Logger.getLogger(BasePersistenceSpec.class.simpleName)

	@Shared Jdbi jdbi
	@Shared Handle handle
	@Shared DataStore datastore;

	def setupSpec() {
		assert jdbi == null
		jdbi = init()
		handle = jdbi.open()
		datastore = handle.attach(DataStore)

		datastore.createUserTable()
		datastore.createMessageTable()
		datastore.createFollowingTable()

		logger.fine "jdbi init"
	}

	private Jdbi init() {
		Logger logger = Logger.getLogger("SQL");
		Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
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

	def cleanupSpec() {
		if(handle != null) {
			// note:: H2 memory database disposes DB data on connection close
			try {
				handle.execute(
						"drop table if exists USERS;"+
						"drop table if exists MESSAGES;"+
						"drop table  if exists FOLLOWINGS;")
			} catch (Exception e) {
				handle.close()
			}
		}
	}
}
