package squawker.jdbi

import static java.time.Instant.now
import static org.hamcrest.CoreMatchers.*

import java.time.Instant

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.statement.UnableToCreateStatementException

import spock.lang.Specification
import spock.lang.Subject
import squawker.Message
import squawker.User


class PersistentUserSpec extends Specification {
	UserStore userStore = Mock(UserStore)
	MessageStore messageStore = Mock(MessageStore)
	FollowingStore followingStore = Mock(FollowingStore)
	

	@Subject 
	PersistentUser user = new PersistentUser(followingStore, messageStore, 1, 'spock', now())

	def "following another user is persisted"() {
		given:
		def other = new User("kirk")

		when:
		user.follow(other)

		then:
		1 * followingStore.follow(user, other)
	}

	def "users cannot follow themselves"() {
		when:
		user.follow(user)

		then:
		thrown(IllegalArgumentException)

		and:
		0 * followingStore.follow(*_)
	}

	def "timeline is fetched from database"() {
		given:
		def poster = new User("kirk")
		def message = new Message(poster, "@spock damage report, Mr Spock", now())

		when:
		def timeline = user.timeline()

		then:
		1 * messageStore.timeline(user) >> [message]

		and:
		timeline == [message]
	}

	def "following list is read from database and cached"() {
		given:
		def otherUsers = ["kirk", "bones", "scotty"].collect { new User(it) }

		when:
		def result1 = user.getFollowing()
		def result2 = user.getFollowing()

		then:
		1 * followingStore.findFollowees(user) >> otherUsers

		and:
		result1 == otherUsers as Set
		result2 == result1
	}

	def "posting a message inserts it to the database"() {
		given:
		def messageText = "Fascinating!"

		when:
		user.post(messageText, now())

		then:
		1 * messageStore.insert(equalTo(user), equalTo(messageText), _)
	}

	def "a message that is too long is not written to the database"() {
		given: "some message text that exceeds the maximum allowed length"
		def messageText = """On my planet, 'to rest' is to rest, to cease using
                         energy. To me it is quite illogical to run up and down
                         on green grass using energy instead of saving it."""

		expect:
		messageText.length() > Message.MAX_TEXT_LENGTH

		when: "a user attempts to post the message"
		user.post(messageText, now())

		then: "an exception is thrown"
		thrown(IllegalArgumentException)

		and: "no attempt is made to write the message to the database"
		0 * _
	}

	def "an exception is thrown if the database connection is stale"() {
		given:
		messageStore._ >> { throw new UnableToCreateStatementException(null) }

		when:
		user.posts()

		then:
		def e = thrown(IllegalStateException)
		e.cause instanceof UnableToCreateStatementException
	}
}
