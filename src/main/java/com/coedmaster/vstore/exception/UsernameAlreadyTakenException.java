package com.coedmaster.vstore.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

	private static final long serialVersionUID = 8914077930178579905L;

	public UsernameAlreadyTakenException() {
		super();
	}

	public UsernameAlreadyTakenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UsernameAlreadyTakenException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsernameAlreadyTakenException(String message) {
		super(message);
	}

	public UsernameAlreadyTakenException(Throwable cause) {
		super(cause);
	}

}
