package squawker

import java.time.Instant


import spock.lang.Shared
import spock.lang.Subject
import squawker.jdbi.BasePersistenceSpec
import squawker.jdbi.PersistentUser

class TimelineSpec extends BasePersistenceSpec {
	
	@Subject
	@Shared PersistentUser user
	@Shared PersistentUser followed
	@Shared PersistentUser non_followed
	
	def setupSpec() {
		 user = datastore.newUser('spock')
		 followed = datastore.newUser('kirk')
		 non_followed = datastore.newUser('khan')
		 
		 user.follow followed
	}
	
	def cleanup() {
		handle.execute('delete from MESSAGES')
	}
	
	def 'a user only sees messages from users they follow in their timeline'() {
		given:
		def message = new Message(postedBy, "Lorem ipsum dolor sit amet", Instant.now())
		datastore.insert(message)
		
		expect:
		user.timeline()*.text.contains(message.text) == shouldAppearInTimeline
		
		where:
		postedBy      | shouldAppearInTimeline
		user          | true
		followed      | true
		non_followed  | false
	}
	
	def 'a user only sees messages from users they follow in their timeline (defining message in where)'() {
		given:
		datastore.insert(message)
		
		expect:
		user.timeline()*.text.contains(message.text) == shouldAppearInTimeline
		
		where:
		postedBy      | shouldAppearInTimeline
		user          | true
		followed      | true
		non_followed  | false
		
		message = new Message(postedBy, "Lorem ipsum dolor sit amet", Instant.now())
	}
	
}
