package squawker.registration


import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import squawker.Message
import squawker.User
import squawker.jdbi.DataStore
import squawker.registration.RegistrationServiceException.InvalidCharactorsInUserNameException
import squawker.registration.RegistrationServiceException.InvalidUserNameException
import squawker.registration.RegistrationServiceException.UserNameAlreadyInUseException

class RegistrationSpec extends Specification {
	DataStore datastore = Mock(DataStore)
	@Subject 
	RegistrationService service = new RegistrationService(datastore);
	@Shared used_username = 'spock'
	
	def 'a valid registration inserts a user to the database'() {
		when:
		def user = service.register('spock')
		
		then:
		1 * datastore.insert(_ as User)
		0 * datastore.insert(_ as Message)
		
		and:
		user.username == 'spock';
	}
	
	// without @Unroll, spock created tabular reports 
	// @Unroll('for username: \'#username\' expected error: \'#error\'')
	def 'check invalid usernames-1'(){ 
		given:
		datastore.usernameInUse('spock') >> true
		
		when:
		service.register(username)
		
		then:
		thrown(error)
		
		and:
		0 *  datastore.insert(_ as User)
		
		where:
		username << [null, '', '     ', '@&%\$+[', 'spock']
		error << [
			InvalidUserNameException,
			InvalidUserNameException,
			InvalidUserNameException,
			InvalidCharactorsInUserNameException,
			UserNameAlreadyInUseException
			]
	}
	// @Unroll('for username: \'#username\' expected error: \'#error\'')
	def 'check invalid usernames-2'(){
		given:
		datastore.usernameInUse('spock') >> true
		
		when:
		service.register(username)
		
		then:
		thrown(error)
		
		and:
		0 *  datastore.insert(_ as User)
		
		where:
		username       | error
		null           | InvalidUserNameException
		''             | InvalidUserNameException
		'     '        | InvalidUserNameException
		'@&%\$+['      | InvalidCharactorsInUserNameException
		'spock'        | UserNameAlreadyInUseException
	}
	// @Unroll('for username: \'#username\' expected error: \'#error\'')
	def 'check invalid usernames(case is irrelavent)'(){
		given:
		datastore.usernameInUse({ used_username.equalsIgnoreCase(it) }) >> true
		
		when:
		service.register(username)
		
		then:
		thrown(error)
		
		and:
		0 *  datastore.insert(_ as User)
		
		where:
		username                           | error
		null                               | InvalidUserNameException
		''                                 | InvalidUserNameException
		'     '                            | InvalidUserNameException
		'@&%\$+['                          | InvalidCharactorsInUserNameException
		used_username                      | UserNameAlreadyInUseException
		used_username.toLowerCase()        | UserNameAlreadyInUseException
		used_username.toUpperCase()        | UserNameAlreadyInUseException
	}
}
