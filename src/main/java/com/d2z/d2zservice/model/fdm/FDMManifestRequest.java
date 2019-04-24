package com.d2z.d2zservice.model.fdm;

public class FDMManifestRequest {

	private String message_no;
	private String customer_id;
	private ArrayOfConsignment consignments;
	public String getMessage_no() {
		return message_no;
	}
	public void setMessage_no(String message_no) {
		this.message_no = message_no;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public ArrayOfConsignment getConsignments() {
		return consignments;
	}
	public void setConsignments(ArrayOfConsignment consignments) {
		this.consignments = consignments;
	}
	
	
	
}
