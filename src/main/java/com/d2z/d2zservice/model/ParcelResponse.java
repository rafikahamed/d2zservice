package com.d2z.d2zservice.model;

public class ParcelResponse {
private String parcelid;
private String hawb;
private String mawb;
private String note;
private String output;
private DropDownModel client;
private DropDownModel pod;
public DropDownModel getClient() {
	return client;
}
public void setClient(DropDownModel client) {
	this.client = client;
}
public DropDownModel getPod() {
	return pod;
}
public void setPod(DropDownModel pod) {
	this.pod = pod;
}
public String getOutput() {
	return output;
}
public void setOutput(String output) {
	this.output = output;
}
public String getParcelid() {
	return parcelid;
}
public void setParcelid(String parcelid) {
	this.parcelid = parcelid;
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
public String getNote() {
	return note;
}
public void setNote(String note) {
	this.note = note;
}

public DropDownModel getStat() {
	return stat;
}
public void setStat(DropDownModel stat) {
	this.stat = stat;
}

private DropDownModel stat;

}
