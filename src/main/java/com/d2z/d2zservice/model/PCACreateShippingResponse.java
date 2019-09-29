package com.d2z.d2zservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //
public class PCACreateShippingResponse {
	
	private int status;
	private String msg;
	private String connote;
	private String ref;
	private String custref;
	private String depot;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getConnote() {
		return connote;
	}
	public void setConnote(String connote) {
		this.connote = connote;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getCustref() {
		return custref;
	}
	public void setCustref(String custref) {
		this.custref = custref;
	}
	public String getDepot() {
		return depot;
	}
	public void setDepot(String depot) {
		this.depot = depot;
	}

}
