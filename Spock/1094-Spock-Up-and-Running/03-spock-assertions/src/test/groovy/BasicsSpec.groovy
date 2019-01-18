import org.spockframework.runtime.HamcrestFacade

import sam.pkg.User
import spock.lang.Specification

class BasicsSpec extends Specification {
	def "assert message demo"() {
		given:
		int a = 1
		int b = 2
		
		System.err.println('hello Standard error')
		
		expect: 'must failed'
		assert a == b : "expected $a was $b"
	}
	
	User user(username) { new User(username) } 
	
	def 'a user can post a message' () {
		given:'a user and a message'
		def user = user("username")
		def msg = '@kirk that is illogical, Captain!' 
		
		when:'user posts a message'
		user.post msg
		
		then: 
		user.posts.size() == 1
		user.posts[0].text == msg
		user.posts*.text == [msg]
	}
	
	
	def 'multiple users test'() {
		given:
		def  kirk = user('kirk')
		def spock = user('spock')
		def scotty = user('scotty')
		
		and:
		def users = [kirk, spock, scotty]
		def msg = '@kirk that is illogical, Captain!'
		
		expect:
		users*.username == ['kirk', 'spock', 'scotty']
	}
}
