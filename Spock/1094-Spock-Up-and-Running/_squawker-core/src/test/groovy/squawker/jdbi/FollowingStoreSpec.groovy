package squawker.jdbi

import spock.lang.Shared
import spock.lang.Subject
import squawker.User

class FollowingStoreSpec extends BasePersistenceSpec {

	@Subject @Shared FollowingStore followingStore

	@Override
	protected void beforeHandleOpen() {
		JdbiInit.registerUserMapper(jdbi)
	}

	def setupSpec() {
		followingStore = handle.attach(FollowingStore)
		followingStore.createTable()
	}

	def cleanupSpec() {
		handle.execute("drop table followings if exists")
	}

	def cleanup() {
		handle.execute("delete from followings")
	}

	def "can fetch a list of users a user is following"() {
		given:
		def user1 = user("spock")
		def followeeUser1 = user("kirk")
		def followeeUser2 = user("bones")
		def nonFollowingUser = user("khan")
		[
			user1,
			followeeUser1,
			followeeUser2,
			nonFollowingUser
		].each {
			userStore.insert(it.username)
		}

		and:
		followingStore.follow(user1, followeeUser1)
		followingStore.follow(user1, followeeUser2)

		expect:
		followingStore.findFollowees(user1) == [followeeUser1, followeeUser2]
	}

	private User user(String username) {
		return new User(username);
	}

	def "can fetch a list of users who are following a user"() {
		given:
		def user1 = user("spock")
		def follower1 = user("kirk")
		def follower2 = user("bones")
		def nonFollower = user("khan")
		[
			user1,
			follower1,
			follower2,
			nonFollower
		].each {
			userStore.insert(it.username)
		}

		and:
		followingStore.follow(follower1, user1)
		followingStore.follow(follower2, user1)

		expect:
		followingStore.findFollowers(user1) == [follower1, follower2]
	}
}

