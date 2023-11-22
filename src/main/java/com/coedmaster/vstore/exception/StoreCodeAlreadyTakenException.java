package com.coedmaster.vstore.exception;

public class StoreCodeAlreadyTakenException extends RuntimeException {

	private static final long serialVersionUID = 3078400964938882555L;

	public StoreCodeAlreadyTakenException() {
		super();
	}

	public StoreCodeAlreadyTakenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StoreCodeAlreadyTakenException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreCodeAlreadyTakenException(String message) {
		super(message);
	}

	public StoreCodeAlreadyTakenException(Throwable cause) {
		super(cause);
	}

}
