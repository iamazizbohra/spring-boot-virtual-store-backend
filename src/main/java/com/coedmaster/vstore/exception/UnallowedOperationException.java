package com.coedmaster.vstore.exception;

public class UnallowedOperationException extends RuntimeException {

	private static final long serialVersionUID = -6245661292512612225L;

	public UnallowedOperationException() {
		super();
	}

	public UnallowedOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnallowedOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnallowedOperationException(String message) {
		super(message);
	}

	public UnallowedOperationException(Throwable cause) {
		super(cause);
	}

}
