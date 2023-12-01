package com.coedmaster.vstore.exception;

public class MobileVerificationCodeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -931761359357561343L;

	public MobileVerificationCodeNotFoundException() {
		super();
	}

	public MobileVerificationCodeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MobileVerificationCodeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MobileVerificationCodeNotFoundException(String message) {
		super(message);
	}

	public MobileVerificationCodeNotFoundException(Throwable cause) {
		super(cause);
	}

}
