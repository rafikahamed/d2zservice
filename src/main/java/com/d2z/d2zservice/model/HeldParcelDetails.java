package com.d2z.d2zservice.model;

public class HeldParcelDetails {
	
	private String mawb;
	private String hawb;
	private String note;
	private String client;
	private String pod;
	private String status;
	private String email;
	
	public HeldParcelDetails(Object[] obj) {
		mawb = (String)obj[0];
		hawb = (String)obj[1];
		note = (String)obj[2];
		client = (String)obj[3];
		pod = (String)obj[4];
		status = (String)obj[5];
		email = (String)obj[6];
	}

	public String getMawb() {
		return mawb;
	}
	public void setMawb(String mawb) {
		this.mawb = mawb;
	}
	public String getHawb() {
		return hawb;
	}
	public void setHawb(String hawb) {
		this.hawb = hawb;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPod() {
		return pod;
	}
	public void setPod(String pod) {
		this.pod = pod;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
