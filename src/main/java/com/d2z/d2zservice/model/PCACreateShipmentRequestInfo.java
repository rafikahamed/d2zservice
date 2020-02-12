package com.d2z.d2zservice.model;

import java.math.BigDecimal;
import java.util.List;

public class PCACreateShipmentRequestInfo {

	private String no;
	private String cust_ref;
	private String cust_ref1;
	private String type;
	private String packs;
	private List<PCAPackages> packages;
	private String weight;
	private String cbm;
	private PCADim dim;
	private String currency;
	private PCAShipper shipper;
	private PCAReceiver receiver;
	private PCAConsignee consignee;
	private List<PCAItems> items;
	private String chargecode;
	private String direct;
	
	public String getDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		this.direct = direct;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getCust_ref() {
		return cust_ref;
	}
	public void setCust_ref(String cust_ref) {
		this.cust_ref = cust_ref;
	}
	public String getCust_ref1() {
		return cust_ref1;
	}
	public void setCust_ref1(String cust_ref1) {
		this.cust_ref1 = cust_ref1;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPacks() {
		return packs;
	}
	public void setPacks(String packs) {
		this.packs = packs;
	}
	public List<PCAPackages> getPackages() {
		return packages;
	}
	public void setPackages(List<PCAPackages> packages) {
		this.packages = packages;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCbm() {
		return cbm;
	}
	public void setCbm(String cbm) {
		this.cbm = cbm;
	}
	public PCADim getDim() {
		return dim;
	}
	public void setDim(PCADim dim) {
		this.dim = dim;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public PCAShipper getShipper() {
		return shipper;
	}
	public void setShipper(PCAShipper shipper) {
		this.shipper = shipper;
	}
	public PCAReceiver getReceiver() {
		return receiver;
	}
	public void setReceiver(PCAReceiver receiver) {
		this.receiver = receiver;
	}
	public PCAConsignee getConsignee() {
		return consignee;
	}
	public void setConsignee(PCAConsignee consignee) {
		this.consignee = consignee;
	}
	public List<PCAItems> getItems() {
		return items;
	}
	public void setItems(List<PCAItems> items) {
		this.items = items;
	}
	public String getChargecode() {
		return chargecode;
	}
	public void setChargecode(String chargecode) {
		this.chargecode = chargecode;
	}
	
}
