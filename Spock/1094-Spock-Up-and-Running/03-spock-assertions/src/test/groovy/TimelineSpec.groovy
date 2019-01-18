import static java.time.Instant.*
import static java.time.ZoneOffset.*
import static java.time.temporal.ChronoUnit.*

import java.time.*

import sam.pkg.Message
import sam.pkg.User
import spock.lang.*

class TimelineSpec extends Specification {
	@Subject User user = new User("khan")
	User followedUser = new User("kirk")
	User otherUser = new User("spock")

	def setup() {
		user.follow(followedUser)
		def now = now()

		postMessage(otherUser, now.minus(6, MINUTES), "His pattern indicates two-dimensional thinking.")
		postMessage(user, now.minus(5, MINUTES), "@kirk You're still alive, my old friend?")
		postMessage(followedUser, now.minus(4, MINUTES), "@khan KHAAANNNN!")
		postMessage(followedUser, now.minus(3, MINUTES), "@scotty I need warp speed in three minutes or we're all dead!")
		postMessage(otherUser, now.minus(2, MINUTES), "@bones I'm sorry, Doctor, I have no time to explain this logically.")
		postMessage(user, now.minus(1, MINUTES), "It is very cold in space!")
	}
	
	private void postMessage(User poster, Instant at, String text) {
		def clock = Clock.fixed(at, UTC)
		poster.post(text, clock.instant())
	}

	def "a user's timeline contains posts from themselves and followed users"() {
		expect:
		/** TODO
		 * Be aware that Spock’s with(Object, Closure) method is not the
		 * same as Groovy’s Object.with(Closure). Accidentally using the
		 * Groovy form in a then: or expect: block will result in the asser‐
		 * tions not working. 
		 */
		with(user.timeline()) {
			size() == 4
			it*.postedBy.every {
				it in [user, followedUser]
			}
			!it*.postedBy.any { it == otherUser }
		}
	}
	def "a user's timeline is ordered most recent first"() {
		expect:
		with(user.timeline()) {
			postedAt == postedAt.sort()
		}
	}
	def "a timeline cannot be modified directly"() {
		when:
		user.timeline() << new Message(user, "@kirk You're still alive, my old friend?", now() )
		then:
		thrown(UnsupportedOperationException)
	}
}
