package squawker.jdbi

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

import org.jdbi.v3.core.mapper.ColumnMapper

import squawker.User

class UserStoreSpec extends BasePersistenceSpec {
	@Override
	protected void beforeHandleOpen() {
		JdbiInit.registerUserMapper(jdbi)
	}

	def 'can retrieve a list of user objects'() {
		given:
		usernames.each {
			handle.execute('insert into users(username, registered) values(?,?)', it, timestamp)
		}

		when:
		def actual = userStore.findAll()

		then:
		with(actual.toList()) {
			username == usernames
			registered.every { it == timestamp }
		}

		where:
		usernames = ['kirk', 'spock']
		timestamp = LocalDateTime.of(1966, 9, 8, 20, 0).toInstant(ZoneOffset.UTC)
	}

	def 'can insert a user object'() {
		given:
		def instant = Instant.now()
		def user = new User('spock', instant)

		expect:
		rowCount('users') == 0

		when:
		userStore.insert(user.username, instant)

		then:
		def map = handle.createQuery('SELECT * FROM users').mapToMap().findOnly()

		and:
		map.username == user.username
		map.registered.time == instant.toEpochMilli()
	}

	def 'can determine if a username is in use'() {
		given:
		userStore.insert('spock')

		expect:
		userStore.usernameInUse(username).present == shouldExist

		where:
		username | shouldExist
		"spock"  | true
		"SPOCK"  | true
		"kirk"   | false
	}
}
