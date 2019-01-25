package squawker.jdbi.async

import java.util.concurrent.Executors

import spock.lang.Shared
import spock.lang.Subject
import squawker.jdbi.BasePersistenceSpec
import squawker.jdbi.FollowingStore
import squawker.jdbi.JdbiInit
import squawker.jdbi.MessageStore
import squawker.jdbi.PersistentUserCache

abstract class BaseAsyncMessageStoreSpec extends BasePersistenceSpec {
	@Shared MessageStore messageStore
	@Shared FollowingStore followingStore
	@Subject AsyncMessageStore asyncMessageStore
	
	PersistentUserCache cache = new PersistentUserCache(new HashMap());
	
	@Override
	protected void beforeHandleOpen() {
		JdbiInit.registerPersistentUserMapper(jdbi, { -> followingStore}, { -> messageStore})
		JdbiInit.registerMessageMapper(jdbi, { -> userStore}, { -> cache})
	}
	
	def setupSpec() {
		messageStore = handle.attach MessageStore
		followingStore = handle.attach FollowingStore
		
		messageStore.createTable()
		followingStore.createTable()
	}
	
	def cleanupSpec() {
		handle.execute(
			'drop table messages if exists;'+
			'drop table followings if exists;')
	}
	
	def setup() {
		cache.clear()
		asyncMessageStore = new AsyncMessageStore(messageStore, Executors.newSingleThreadExecutor())
	}
	def cleanup() {
		handle.execute(
			'delete from messages;'
			+'delete from followings;')
	}
}
