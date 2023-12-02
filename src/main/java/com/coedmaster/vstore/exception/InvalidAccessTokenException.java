package com.coedmaster.vstore.exception;

public class InvalidAccessTokenException extends RuntimeException {

	private static final long serialVersionUID = -6630784916967834354L;

	public InvalidAccessTokenException() {
		super();
	}

	public InvalidAccessTokenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidAccessTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAccessTokenException(String message) {
		super(message);
	}

	public InvalidAccessTokenException(Throwable cause) {
		super(cause);
	}

}
