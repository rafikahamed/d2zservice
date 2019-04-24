package com.d2z.d2zservice.model;

public class ExportDelete {
	
private String reference_number;
private String barcodelabelNumber;
private String brokername;
private SenderData senderdata;
public String getReference_number() {
	return reference_number;
}
public SenderData getSenderdata() {
	return senderdata;
}
public void setSenderdata(SenderData senderdata) {
	this.senderdata = senderdata;
}
public void setReference_number(String reference_number) {
	this.reference_number = reference_number;
}
public String getBarcodelabelNumber() {
	return barcodelabelNumber;
}
public void setBarcodelabelNumber(String barcodelabelNumber) {
	this.barcodelabelNumber = barcodelabelNumber;
}
public String getBrokername() {
	return brokername;
}
public void setBrokername(String brokername) {
	this.brokername = brokername;
}

}
