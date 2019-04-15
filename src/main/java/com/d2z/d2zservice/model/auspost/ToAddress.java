package com.d2z.d2zservice.model.auspost;

import java.util.ArrayList;
import java.util.List;

public class ToAddress {
	private String name;
	private List<String> lines;
	private String suburb;
	private String postcode;
	private String state;
	private String email;
	private String phone;
	private String delivery_instructions;
	
	public String getDelivery_instructions() {
		return delivery_instructions;
	}
	public void setDelivery_instructions(String delivery_instructions) {
		this.delivery_instructions = delivery_instructions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getLines() {
		if(null == lines) {
			lines = new ArrayList<String>();
		}

		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	public String getSuburb() {
		return suburb;
	}
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
