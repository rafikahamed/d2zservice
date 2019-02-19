package com.d2z.d2zservice.exception;

public class MaxSizeCountException extends RuntimeException {
	private String userName;
	
	public MaxSizeCountException(String ex) {
		super(ex);
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}