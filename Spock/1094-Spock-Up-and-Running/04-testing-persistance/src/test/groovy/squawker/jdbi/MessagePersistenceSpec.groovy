package squawker.jdbi

import java.lang.reflect.Constructor
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Logger

import org.jdbi.v3.core.mapper.reflect.ConstructorMapper

import squawker.Message
import squawker.User
import squawker.jdbi.DataStore.MessageMapper

class MessagePersistenceSpec extends BasePersistenceSpec {
	private final static Logger logger = Logger.getLogger(MessagePersistenceSpec.class.simpleName);

	User kirk = kirk();
	User spark = spark();
	
	def setup() {
		handle.execute DataStore.CREATE_USER_TABLE
		handle.execute DataStore.CREATE_MESSAGE_TABLE
		
		datastore.insert(spark);
		datastore.insert(kirk);
		
		kirk = datastore.getUserByUsername(kirk.username);
		spark = datastore.getUserByUsername(spark.username);
	}
	
	def 'ensure MessageMapper working'() {
		given:
		Instant registered0 = LocalDateTime.now().minusDays(10).toInstant(ZoneOffset.UTC);
		Instant postedAt0 = LocalDateTime.now().toInstant(ZoneOffset.UTC);
		
		and:
		handle.execute("INSERT INTO USERS(id, username, registered) VALUES(?,?,?)", 200, "username", registered0)
		handle.execute("INSERT INTO MESSAGES(id, posted_by, _text, posted_at) VALUES(?,?,?,?)", 100, 200, "text", postedAt0)
		
		when:
		def list = datastore.postsBy(new User("username"))
		logger.fine(list.toString())
		
		then:
		list.size() == 1
		def actual = list[0]
		
		with(actual) {
			id == 100
			text == "text"
			postedAt == postedAt0
		}
		
		with(actual.postedBy) {
			id == 200
			username == 'username'
			registered == registered0
		}
	}

	def 'can insert a mesage object' () {
		given:
		def clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
		def msg_string = '@bones I was merely stating a fact, Doctor.'
		Message expected = kirk.post(msg_string, clock.instant())
		handle.registerRowMapper(new MessageMapper())
		
		when:
		datastore.insert(expected);

		then:
		Message actual = handle.createQuery("SELECT u.id as uid, m.id as mid, * FROM MESSAGES m LEFT JOIN USERS u ON m.posted_by = u.id").mapTo(Message).findOnly()
		logger.fine("\n  expected : "+expected +
			        "\n  actual   : "+actual)
		with(actual) {
			postedBy.id == kirk.id
			postedBy.username == kirk.username
			postedBy.registered == kirk.registered
			
			text == msg_string
			postedAt == expected.postedAt
		}
	}
	
	def 'can retrieve a list of messages posted by a user'() {
		given:
		insertMessage(kirk, "@khan KHAAANNN!")
		insertMessage(spark, "Fascinating!")
		insertMessage(spark, "@kirk That is illogical, Captain.")
		
		when:
		List<Message> kirk_msgs  = datastore.postsBy(kirk)
		List<Message> spark_msgs  = datastore.postsBy(spark)
		
		logger.fine kirk_msgs.toString()
		logger.fine spark_msgs.toString()
		
		then:
		kirk_msgs.size() == 1
		spark_msgs.size() == 2
		
		and:
		kirk_msgs*.postedBy == [kirk]
		spark_msgs*.postedBy == [spark, spark]
	}
	
	private void insertMessage(User user, String text) {
		datastore.insert(user.post(text))
	}
}
