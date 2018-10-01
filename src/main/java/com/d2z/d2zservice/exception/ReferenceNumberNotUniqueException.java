package com.d2z.d2zservice.exception;

import java.util.List;

public class ReferenceNumberNotUniqueException extends Exception {
private List<String> referenceNumbers;
	
	public ReferenceNumberNotUniqueException(String ex,List<String> referenceNumbers) {
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
