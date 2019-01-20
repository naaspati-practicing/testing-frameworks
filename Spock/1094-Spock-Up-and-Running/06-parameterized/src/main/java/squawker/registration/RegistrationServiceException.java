package squawker.registration;

public class RegistrationServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RegistrationServiceException() { super(); }
	public RegistrationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
	public RegistrationServiceException(String message, Throwable cause) { super(message, cause); }
	public RegistrationServiceException(String message) { super(message); }
	public RegistrationServiceException(Throwable cause) { super(cause); }

	public static class InvalidUserNameException extends RegistrationServiceException {
		private static final long serialVersionUID = 1L;
		
		public InvalidUserNameException() { super(); }
		public InvalidUserNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
		public InvalidUserNameException(String message, Throwable cause) { super(message, cause); }
		public InvalidUserNameException(String message) { super(message); }
		public InvalidUserNameException(Throwable cause) { super(cause); }
	}
	
	public static class InvalidCharactorsInUserNameException extends RegistrationServiceException {
		private static final long serialVersionUID = 1L;
		
		public InvalidCharactorsInUserNameException() { super(); }
		public InvalidCharactorsInUserNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
		public InvalidCharactorsInUserNameException(String message, Throwable cause) { super(message, cause); }
		public InvalidCharactorsInUserNameException(String message) { super(message); }
		public InvalidCharactorsInUserNameException(Throwable cause) { super(cause); }
	}
	
	public static class UserNameAlreadyInUseException extends RegistrationServiceException {
		private static final long serialVersionUID = 1L;
		
		public UserNameAlreadyInUseException() { super(); }
		public UserNameAlreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
		public UserNameAlreadyInUseException(String message, Throwable cause) { super(message, cause); }
		public UserNameAlreadyInUseException(String message) { super(message); }
		public UserNameAlreadyInUseException(Throwable cause) { super(cause); }
	}
	

}
