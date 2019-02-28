package com.d2z.d2zservice.model.etower;

import java.util.List;

public class CreateShippingResponse {

	private String status;
	private List<ResponseData> data;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ResponseData> getData() {
		return data;
	}
	public void setData(List<ResponseData> data) {
		this.data = data;
	}
	
	
}
