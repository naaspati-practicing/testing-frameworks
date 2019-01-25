package squawker.jdbi

import java.time.Instant

import spock.lang.Shared
import spock.lang.Subject
import squawker.User

class MessageStoreSpec extends BasePersistenceSpec {

	@Subject
	@Shared
	MessageStore messageStore
	def kirk = kirk()
	def spock = spark()
	
	PersistentUserCache cache = new PersistentUserCache()

	@Override
	protected void beforeHandleOpen() {
		JdbiInit.registerPersistentUserMapper(jdbi, {-> null}, { -> messageStore})
		JdbiInit.registerMessageMapper(jdbi, {-> userStore}, {-> cache})
	}	

	def setupSpec() {
		messageStore = handle.attach(MessageStore)
		messageStore.createTable()
	}

	def cleanupSpec() {
		handle.execute("drop table messages if exists")
	}

	def setup() {
		[kirk, spock].each { userStore.insert(it.username) }
	}

	def cleanup() {
		handle.execute("delete from messages")
	}

	def "can retrieve a list of messages posted by a user"() {
		given:
		insertMessage(kirk, "@khan KHAAANNN!")
		insertMessage(spock, "Fascinating!")
		insertMessage(spock, "@kirk That is illogical, Captain.")

		when:
		def posts = messageStore.postsBy(spock)

		then:
		posts*.postedBy == [spock, spock]
	}

	def "can insert a message"() {
		given:
		def instant = Instant.now()

		when:
		def message = messageStore.insert(spock, "@bones I was merely stating a fact, Doctor.", instant)

		then:
		def map = handle.createQuery('''select u.username, m.text, m.posted_at
                                         from messages m, users u
                                         where m.posted_by = u.id''')
				.mapToMap()
				.findOnly()

		then:
		map.text == message.text
		map.username == message.postedBy.username
		map.posted_at.time == instant.toEpochMilli()

	}

	private void insertMessage(User postedBy, String text) {
		handle.createUpdate('''insert into messages
                              (posted_by, text, posted_at)
                              select id, ?, ? from users where username = ?''')
				.bind(0, text)
				.bind(1, Instant.now())
				.bind(2, postedBy.username)
				.execute()
	}
}