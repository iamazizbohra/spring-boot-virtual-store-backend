package com.coedmaster.vstore.exception;

public class EntityAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 8330733810001340543L;

	public EntityAlreadyExistsException() {
		super();
	}

	public EntityAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EntityAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityAlreadyExistsException(String message) {
		super(message);
	}

	public EntityAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
