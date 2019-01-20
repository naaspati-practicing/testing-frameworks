package squawker.jdbi

import java.lang.reflect.Constructor
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Logger

import org.codehaus.groovy.runtime.typehandling.LongMath
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.jdbi.v3.core.statement.UnableToCreateStatementException

import spock.lang.Specification
import spock.lang.Subject
import squawker.Message
import squawker.User

class PersistentUserSpec2 extends Specification {
	private final static Logger logger = Logger.getLogger(PersistentUserSpec2.class.simpleName);
	
	DataStore datastore 
	@Subject PersistentUser user
	
	def setup() {
		datastore = Mock(DataStore)
		user = new PersistentUser("username", Instant.now())
		user.setDataStore(datastore)
	}

	def 'posting a message inserts it to the database'(){
		given:
		def messageText = 'Facinating!'
		
		when:
		user.post(messageText)
		
		then:
		1 * datastore.insert({messageText == it.text})
	}
	
	def 'a message that is too long is not written to the database' () {
		given: 'some message text that exceeds the maximum allowed length'
		def longMsg = '''On my planet, 'to rest' is to rest, to cease using
                         energy. To me it is quite illogical to run up and down
                         on green grass using energy instead of saving it.''';
						 
		expect:
		longMsg.length() > Message.MAX_TEXT_LENGTH
		
		when:'a user attempts to post the message'
		user.post(longMsg)
		
		then:'an exception is thrown'
		thrown(IllegalArgumentException)
		
		and: 'no attempt is made to write the message to the database'
		0 * datastore.insert(_)					 
	}
	
	def 'users cannot follow themselves'() {
		when:
		user.follow(user)
		
		then:
		thrown(IllegalArgumentException)
		
		and:
		0 * datastore.follow(*_)
	}
	
	def 'an exception is thrown if the database connection is stale'() {
		
	}
}
