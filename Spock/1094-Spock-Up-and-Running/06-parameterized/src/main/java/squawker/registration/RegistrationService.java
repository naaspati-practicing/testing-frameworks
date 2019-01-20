package squawker.registration;

import squawker.User;
import squawker.jdbi.DataStore;
import squawker.registration.RegistrationServiceException.InvalidCharactorsInUserNameException;
import squawker.registration.RegistrationServiceException.InvalidUserNameException;
import squawker.registration.RegistrationServiceException.UserNameAlreadyInUseException;

public class RegistrationService {
	private final DataStore datastore;

	public RegistrationService(DataStore datastore) {
		this.datastore = datastore;
	}
	
	public User register(String username) {
		if(username == null)
			throw new InvalidUserNameException(new NullPointerException("username cannot be null"));
		if(username.trim().isEmpty())
			throw new InvalidUserNameException("username cannot be empty/contain only spaces");
		if(!username.chars().allMatch(this::check)) 
			throw new InvalidCharactorsInUserNameException();
		
		if(datastore.usernameInUse(username))
			throw new UserNameAlreadyInUseException();
		
		User user = new User(username);
		datastore.insert(user);
		return user;
	}
	private boolean check(int cn) {
		if(cn == '_')
			return true;
		if(cn >= 'a' && cn <= 'z')
			return true;
		if(cn >= 'A' && cn <= 'Z')
			return true;
		if(cn >= '0' && cn <= '9')
			return true;
		
		return false;
		
	}
	

}
