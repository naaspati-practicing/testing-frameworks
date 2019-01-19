package squawker.jdbi

import java.lang.reflect.Constructor
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Logger

import org.jdbi.v3.core.mapper.reflect.ConstructorMapper

import squawker.User

class UserPersistenceSpec extends BasePersistenceSpec {
	private final static Logger logger = Logger.getLogger(UserPersistenceSpec.class.simpleName);

	def setup() {
		handle.execute DataStore.CREATE_USER_TABLE
	}
	
	def 'can retrieve a list of user objects'() {
		given:
		def timestamp = LocalDateTime.of(1966, 9, 8, 20, 0).toInstant(ZoneOffset.UTC)
		def expected_usernames = ["kirk", "spock"]
		expected_usernames.each {
			handle.execute("INSERT INTO USERS(username, registered) VALUES (?,?)", it, timestamp)
		}

		when:
		def users = datastore.allUsers()

		then:
		with(users) {
			username == expected_usernames
			registered.every { it == timestamp }
		}
	}

	def 'can insert a user Object' () {
		given:
		handle.registerRowMapper(ConstructorMapper.factory(User))
		def clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

		and:
		User expected = new User("spock", clock.instant())

		when:
		datastore.insert(expected);

		then:
		User actual = handle.createQuery("SELECT * FROM USERS").mapTo(User).findOnly()
		with(actual) {
			username == expected.username
			registered == expected.registered
		}
	}
	
	def 'can get inserted user object by username'() {
		given:
		User kirk = kirk()
		
		when:
		datastore.insert(kirk)
		
		then:
		User actual = datastore.getUserByUsername(kirk.username)
		
		with(actual) {
			username == kirk.username
			registered == kirk.registered
		} 
	}
	
}
