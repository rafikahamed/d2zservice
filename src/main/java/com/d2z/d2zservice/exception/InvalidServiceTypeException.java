package com.d2z.d2zservice.exception;

import java.util.List;

public class InvalidServiceTypeException extends RuntimeException {
	private List<String> referenceNumbers;
	
	public InvalidServiceTypeException(String ex,List<String> referenceNumbers) {
		super(ex);
		this.referenceNumbers = referenceNumbers;
	}

	public List<String> getReferenceNumbers() {
		return referenceNumbers;
	}

	public void setReferenceNumbers(List<String> referenceNumbers) {
		this.referenceNumbers = referenceNumbers;
	}

}
