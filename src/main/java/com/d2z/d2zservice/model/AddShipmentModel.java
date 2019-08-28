package com.d2z.d2zservice.model;

import java.util.List;
import java.util.Set;

public class AddShipmentModel {
	private List<DropDownModel> Mlid;
	
	public List<DropDownModel> getMlid() {
		return Mlid;
	}
	public void setMlid(List<DropDownModel> mlid) {
		Mlid = mlid;
	}
	public DropDownModel getBrokerName() {
		return BrokerName;
	}
	public void setBrokerName(DropDownModel brokerName) {
		BrokerName = brokerName;
	}
	public List<DropDownModel> getConsignee() {
		return Consignee;
	}
	public void setConsignee(List<DropDownModel> consignee) {
		Consignee = consignee;
	}
	private DropDownModel BrokerName;
	private List<DropDownModel> Consignee;
	

}
