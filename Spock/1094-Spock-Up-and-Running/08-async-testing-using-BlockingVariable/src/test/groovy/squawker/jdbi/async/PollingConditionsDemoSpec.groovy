package squawker.jdbi.async

import java.time.Instant

import spock.util.concurrent.BlockingVariables
import spock.util.concurrent.PollingConditions
import squawker.jdbi.JdbiInit

/**
 * An alternative approach to the use of blocking constructs to capture values generated
 * asynchronously is to poll for an expected result. Spock includes the PollingCondi
 * tions class in order to accommodate this approach.
 * The PollingConditions class provides two methods: within(double, Closure) and
 * eventually(Closure). Both methods return true if any assertions made within the
 * closure pass before the timeout expires. The within method requires a specified
 * timeout in seconds, whereas eventually uses the default timeout of the PollingCon
 * ditions object.
 *
 */
class PollingConditionsDemoSpec extends BaseAsyncMessageStoreSpec {
	def 'can retrieve latest message by all followers'() {
		given:
		def user = userStore.insert(username)
		followedUsernames.each {
			def followee = userStore.insert(it)
			user.follow(followee)
			followee.post("Older message", Instant.now().minusSeconds(5))
			followee.post("Hi @$username from @$it", Instant.now())
		}

		expect:
		user.following*.username.containsAll(followedUsernames)

		when:
		def messages = []
		asyncMessageStore.latestPostsByFollowed(user) {msg,exception -> messages << msg}

		then:
		def condition = new PollingConditions()
		condition.eventually {
			assert messages.text.containsAll(expectedMessages)
		}

		where:
		username = "spock"
		followedUsernames = ["kirk", "bones", "sulu"]
		expectedMessages = followedUsernames.collect {  "Hi @$username from @$it".toString()  }
	}
}
