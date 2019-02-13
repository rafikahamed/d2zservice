package com.d2z.d2zservice.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class DeleteConsignmentRequest {

	@NotEmpty(message = "UserName cannot be empty")
	private String userName;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	private List<String> referenceNumbers;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getReferenceNumbers() {
		return referenceNumbers;
	}
	public void setReferenceNumbers(List<String> referenceNumbers) {
		this.referenceNumbers = referenceNumbers;
	}
}
