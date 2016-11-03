package com.sapient.exceptions;

public class ReportGenerationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1513072791318930181L;

	public ReportGenerationException(String msg, Throwable t) {
		super(msg, t);
	}

	public ReportGenerationException(String msg) {
		super(msg);
	}

	public ReportGenerationException(Throwable t) {
		super(t);
	}
}
