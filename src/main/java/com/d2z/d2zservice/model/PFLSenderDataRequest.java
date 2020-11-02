package com.d2z.d2zservice.model;

import java.util.List;

public class PFLSenderDataRequest {
	
	private List<SenderDataApi> pflSenderDataApi;
	private List<SenderDataApi> nonPflSenderDataApi;
	private List<SenderDataApi> etowerSenderData;
	
	public List<SenderDataApi> getEtowerSenderData() {
		return etowerSenderData;
	}
	public void setEtowerSenderData(List<SenderDataApi> etowerSenderData) {
		this.etowerSenderData = etowerSenderData;
	}
	public List<SenderDataApi> getPflSenderDataApi() {
		return pflSenderDataApi;
	}
	public void setPflSenderDataApi(List<SenderDataApi> pflSenderDataApi) {
		this.pflSenderDataApi = pflSenderDataApi;
	}
	public List<SenderDataApi> getNonPflSenderDataApi() {
		return nonPflSenderDataApi;
	}
	public void setNonPflSenderDataApi(List<SenderDataApi> nonPflSenderDataApi) {
		this.nonPflSenderDataApi = nonPflSenderDataApi;
	}
	
}
