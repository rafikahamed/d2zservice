package com.d2z.d2zservice.exception;

public class InvalidUserException extends RuntimeException {
	private String userName;
	
	public InvalidUserException(String ex,String userName) {
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
