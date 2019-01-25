package squawker.jdbi

import java.util.logging.Logger

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

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
		jdbi = JdbiInit.init("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", Logger.getLogger("SQL"))
		beforeHandleOpen();
		handle = jdbi.open()
		userStore = handle.attach(UserStore)
		userStore.createTable()

		logger.fine "jdbi init"
	}
	
	protected void beforeHandleOpen() {
		
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
