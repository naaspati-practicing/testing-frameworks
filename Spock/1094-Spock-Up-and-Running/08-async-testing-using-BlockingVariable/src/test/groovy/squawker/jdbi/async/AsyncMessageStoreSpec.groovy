package squawker.jdbi.async

import java.time.Instant

import spock.util.concurrent.BlockingVariable

class AsyncMessageStoreSpec extends BaseAsyncMessageStoreSpec {
	def 'blocking callback'() {
		given:
		def user = userStore.insert('spock')

		and:
		messageStore.insert(user, msgTxt, posted_at)

		when:
		def value = new BlockingVariable()
		asyncMessageStore.latestPostBy(user) {msg,exception -> value.set([msg, exception])}

		then:
		def gt = value.get()
		gt[1] == null
		gt[0].text == msgTxt

		where:
		msgTxt = 'Fascinating!'
		posted_at = Instant.now()
	}
}
