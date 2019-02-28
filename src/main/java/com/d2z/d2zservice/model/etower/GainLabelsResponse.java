package com.d2z.d2zservice.model.etower;

import java.util.List;

public class GainLabelsResponse {

	private String status;
	private List<LabelData> data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<LabelData> getData() {
		return data;
	}
	public void setData(List<LabelData> data) {
		this.data = data;
	}
	
}
