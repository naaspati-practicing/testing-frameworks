package squawker.jdbi.async

import java.time.Instant

import spock.util.concurrent.BlockingVariables
import squawker.jdbi.JdbiInit

class BlockingVariablesDemoSpec extends BaseAsyncMessageStoreSpec {
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
		def messages = new BlockingVariables()
		asyncMessageStore.latestPostsByFollowed(user) {msg,exception -> messages[msg.postedBy.username] = [msg, exception]}

		then:
		followedUsernames.every {
			def d = messages[it] 
			d[1] == null
		    d[0].text == "Hi @$username from @$it"
		}
		
		where:
		username = "spock"
        followedUsernames = ["kirk", "bones", "sulu"]
	}
}
