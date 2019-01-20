package squawker.jdbi

import java.lang.reflect.Constructor
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Logger

import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.jdbi.v3.core.statement.UnableToCreateStatementException

import spock.lang.Specification
import spock.lang.Subject
import squawker.User

class PersistentUserSpec extends Specification {
	private final static Logger logger = Logger.getLogger(PersistentUserSpec.class.simpleName);
	
	DataStore datastore 
	@Subject PersistentUser user
	
	def setup() {
		datastore = Mock(DataStore)
		user = new PersistentUser("username", Instant.now())
		user.setDataStore(datastore)
	}

	def 'following another user is persisted'() {
		given:
		def other = new User("Kirk")
		
		when:
		user.follow(other)
		
		then:
		1 * datastore.follow(user, other)
	}
	
	def 'the following list is read from the database and cached' () {
		given: 
		def others = ['kirk', 'bones','scotty'].collect { new User(it) }
		
		when: 'the list of followed users is requested multiple times'
		def result1 = user.following
		def result2 = user.following
		
		then: 'the database is queried only once'
		1 * datastore.findFollowees(user) >> others
		
		and: 'both calls return consistent results'
		result1.size() == 3
		result1 == others as Set
		result2 == result1
	}
	
	def 'an exception is thrown if the database connection is stale'() {
		when: 
		user.posts
		
		then:
		1 * datastore.postsBy(user) >> { throw new UnableToCreateStatementException(null) }
		
		and:
		def e = thrown(UnableToCreateStatementException)
		e instanceof UnableToCreateStatementException
	}
}
