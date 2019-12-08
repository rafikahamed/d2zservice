package com.d2z.d2zservice.model;

public class HeldParcel {
	private String hawb;
	private String mawb;
	private String stat;
	private String note;
	private String client;
	private String pod;
	
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
	public String getHawb() {
		return hawb;
	}
	public void setHawb(String hawb) {
		this.hawb = hawb;
	}
	public String getMawb() {
		return mawb;
	}
	public void setMawb(String mawb) {
		this.mawb = mawb;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

}
