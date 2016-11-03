package com.sapient.exceptions;

public class ParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1401710649725203357L;

	public ParseException(String msg, Throwable t) {
		super(msg, t);
	}

	public ParseException(String msg) {
		super(msg);
	}

	public ParseException(Throwable t) {
		super(t);
	}
}
