package com.d2z.d2zservice.model;

import java.util.List;

public class PflCreateShippingRequest {
	
	private List<PflCreateShippingOrderInfo> orderinfo;

	public List<PflCreateShippingOrderInfo> getOrderinfo() {
		return orderinfo;
	}

	public void setOrderinfo(List<PflCreateShippingOrderInfo> orderinfo) {
		this.orderinfo = orderinfo;
	}
	
	

}
