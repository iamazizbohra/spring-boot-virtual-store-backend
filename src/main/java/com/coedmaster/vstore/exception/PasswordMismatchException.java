package com.coedmaster.vstore.exception;

public class PasswordMismatchException extends RuntimeException {

	private static final long serialVersionUID = 6386669536598548370L;

	public PasswordMismatchException() {
		super();
	}

	public PasswordMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PasswordMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordMismatchException(String message) {
		super(message);
	}

	public PasswordMismatchException(Throwable cause) {
		super(cause);
	}

}
