package com.coedmaster.vstore.exception;

public class InvalidMobileVerificationCodeException extends RuntimeException {

	private static final long serialVersionUID = -6147124630605841039L;

	public InvalidMobileVerificationCodeException() {
		super();
	}

	public InvalidMobileVerificationCodeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidMobileVerificationCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMobileVerificationCodeException(String message) {
		super(message);
	}

	public InvalidMobileVerificationCodeException(Throwable cause) {
		super(cause);
	}

}
