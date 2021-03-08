package com.d2z.d2zservice.model.fdm;

public class LabelRequest {
private String customer_id = "D2Z";
private String refno;
private String cust_suburb;
private String cust_postcode;
private String cust_state;
private String cust_country;
private int number_of_labels;
public String getCustomer_id() {
	return customer_id;
}
public void setCustomer_id(String customer_id) {
	this.customer_id = customer_id;
}
public String getRefno() {
	return refno;
}
public void setRefno(String refno) {
	this.refno = refno;
}
public String getCust_suburb() {
	return cust_suburb;
}
public void setCust_suburb(String cust_suburb) {
	this.cust_suburb = cust_suburb;
}
public String getCust_postcode() {
	return cust_postcode;
}
public void setCust_postcode(String cust_postcode) {
	this.cust_postcode = cust_postcode;
}
public String getCust_state() {
	return cust_state;
}
public void setCust_state(String cust_state) {
	this.cust_state = cust_state;
}
public String getCust_country() {
	return cust_country;
}
public void setCust_country(String cust_country) {
	this.cust_country = cust_country;
}
public int getNumber_of_labels() {
	return number_of_labels;
}
public void setNumber_of_labels(int number_of_labels) {
	this.number_of_labels = number_of_labels;
}


}
