package com.coedmaster.vstore.exception;

public class AccountInactiveException extends RuntimeException {

	private static final long serialVersionUID = -1198728503330291339L;

	public AccountInactiveException() {
		super();
	}

	public AccountInactiveException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AccountInactiveException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountInactiveException(String message) {
		super(message);
	}

	public AccountInactiveException(Throwable cause) {
		super(cause);
	}

}
