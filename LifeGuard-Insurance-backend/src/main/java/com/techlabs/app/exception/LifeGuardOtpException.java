package com.techlabs.app.exception;

public class LifeGuardOtpException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LifeGuardOtpException(String message) {
		super(message);
	}

	public static class ResourceNotFoundException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public ResourceNotFoundException(String message) {
			super(message);
		}
	}

	public static class ResourceAlreadyDeactivedException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public ResourceAlreadyDeactivedException(String message) {
			super(message);
		}
	}

	public static class ResourceNotActiveException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public ResourceNotActiveException(String message) {
			super(message);
		}
	}

	public static class DocumentNotVerifiedException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public DocumentNotVerifiedException(String message) {
			super(message);
		}
	}

	public static class UserNotFoundException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public UserNotFoundException(String message) {
			super(message);
		}
	}

	public static class UserAlreadyActivatedException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public UserAlreadyActivatedException(String message) {
			super(message);
		}
	}

	public static class UserAlreadyDeActivatedException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public UserAlreadyDeActivatedException(String message) {
			super(message);
		}
	}

	public static class UserNotActiveException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public UserNotActiveException(String message) {
			super(message);
		}
	}

	public static class SchemeNotActiveException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public SchemeNotActiveException(String message) {
			super(message);
		}
	}

	public static class PlanNotActiveException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public PlanNotActiveException(String message) {
			super(message);
		}
	}

	public static class SchemeNotFoundException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public SchemeNotFoundException(String message) {
			super(message);
		}
	}

	public static class PlanNotFoundException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public PlanNotFoundException(String message) {
			super(message);
		}
	}

	public static class NoUserFoundException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public NoUserFoundException(String message) {
			super(message);
		}
	}

	public static class NoTransactionsFoundException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public NoTransactionsFoundException(String message) {
			super(message);
		}
	}

	public static class UserNotAssociatedException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public UserNotAssociatedException(String message) {
			super(message);
		}
	}

	public static class AccountNotActiveException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public AccountNotActiveException(String message) {
			super(message);
		}
	}

	public static class AccountDoesNotBelongToUserException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public AccountDoesNotBelongToUserException(String message) {
			super(message);
		}
	}

	public static class AccountAlreadyActiveException extends LifeGuardOtpException {
		private static final long serialVersionUID = 1L;

		public AccountAlreadyActiveException(String message) {
			super(message);
		}
	}

}