package com.d2z.d2zservice.model;

import java.util.List;

public class MCSSenderDataRequest {
	
	private List<SenderDataApi> pflSenderData;
	private List<SenderDataApi> fastwaySenderData;
	private List<SenderDataApi> eparcelSenderData;
	private List<SenderData> pflSenderDataUI;
	private List<SenderData> fastwaySenderDataUI;
	private List<SenderData> eparcelSenderDataUI;
	
	public List<SenderData> getPflSenderDataUI() {
		return pflSenderDataUI;
	}
	public void setPflSenderDataUI(List<SenderData> pflSenderDataUI) {
		this.pflSenderDataUI = pflSenderDataUI;
	}
	public List<SenderData> getFastwaySenderDataUI() {
		return fastwaySenderDataUI;
	}
	public void setFastwaySenderDataUI(List<SenderData> fastwaySenderDataUI) {
		this.fastwaySenderDataUI = fastwaySenderDataUI;
	}
	public List<SenderData> getEparcelSenderDataUI() {
		return eparcelSenderDataUI;
	}
	public void setEparcelSenderDataUI(List<SenderData> eparcelSenderDataUI) {
		this.eparcelSenderDataUI = eparcelSenderDataUI;
	}
	public List<SenderDataApi> getPflSenderData() {
		return pflSenderData;
	}
	public void setPflSenderData(List<SenderDataApi> pflSenderData) {
		this.pflSenderData = pflSenderData;
	}
	public List<SenderDataApi> getFastwaySenderData() {
		return fastwaySenderData;
	}
	public void setFastwaySenderData(List<SenderDataApi> fastwaySenderData) {
		this.fastwaySenderData = fastwaySenderData;
	}
	public List<SenderDataApi> getEparcelSenderData() {
		return eparcelSenderData;
	}
	public void setEparcelSenderData(List<SenderDataApi> eparcelSenderData) {
		this.eparcelSenderData = eparcelSenderData;
	}
	

}
