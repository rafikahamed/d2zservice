package com.d2z.d2zservice.model.auspost;

import java.util.ArrayList;
import java.util.List;

public class FromAddress {

	private String name = "FDM";
	private List<String> lines;
	private String suburb = "Clayton";
	private String postcode = "3168";
	private String state = "VIC";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getLines() {
		if(null == lines) {
			lines = new ArrayList<String>();
			lines.add("5 buckland st");
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
	
	
}
