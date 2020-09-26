package com.d2z.d2zservice.model;

import java.util.List;

public class PflPrintLabelRequest {
	
	private String returnType = "pdf";
	private String printType = "auspost";
	private List<String> ids;
	
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getPrintType() {
		return printType;
	}
	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	

}
