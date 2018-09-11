package com.d2z.d2zservice.model;

import java.awt.image.BufferedImage;

public class SenderData {
	
	private int senderFileId;
	private String referenceNumber;
	private String consigneeName;
	private String consigneeAddr1;
	private String consigneeSuburb;
	private String consigneeState;
	private String consigneePostcode;
	private String consigneePhone;
	private String weight;
	private String shipperName;
	private String shipperAddr1;
	private String shipperAddr2;
	private String shipperCity;
	private String shipperState;
	private String shipperCountry;
	private String shipperPostcode;
	private String barcodeLabelNumber;
	private String datamatrix;
	private String injectionState;
	private BufferedImage datamatrixImage;

	public BufferedImage getDatamatrixImage() {
		return datamatrixImage;
	}

	public void setDatamatrixImage(BufferedImage datamatrixImage) {
		this.datamatrixImage = datamatrixImage;
	}

	public String getInjectionState() {
		return injectionState;
	}

	public void setInjectionState(String injectionState) {
		this.injectionState = injectionState;
	}

	public int getSenderFileId() {
		return senderFileId;
	}

	public void setSenderFileId(int senderFileId) {
		this.senderFileId = senderFileId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeAddr1() {
		return consigneeAddr1;
	}

	public void setConsigneeAddr1(String consigneeAddr1) {
		this.consigneeAddr1 = consigneeAddr1;
	}

	public String getConsigneeSuburb() {
		return consigneeSuburb;
	}

	public void setConsigneeSuburb(String consigneeSuburb) {
		this.consigneeSuburb = consigneeSuburb;
	}

	public String getConsigneeState() {
		return consigneeState;
	}

	public void setConsigneeState(String consigneeState) {
		this.consigneeState = consigneeState;
	}

	public String getConsigneePostcode() {
		return consigneePostcode;
	}

	public void setConsigneePostcode(String consigneePostcode) {
		this.consigneePostcode = consigneePostcode;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getShipperName() {
		return shipperName;
	}

	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}

	public String getShipperAddr1() {
		return shipperAddr1;
	}

	public void setShipperAddr1(String shipperAddr1) {
		this.shipperAddr1 = shipperAddr1;
	}

	public String getShipperAddr2() {
		return shipperAddr2;
	}

	public void setShipperAddr2(String shipperAddr2) {
		this.shipperAddr2 = shipperAddr2;
	}

	public String getShipperCity() {
		return shipperCity;
	}

	public void setShipperCity(String shipperCity) {
		this.shipperCity = shipperCity;
	}

	public String getShipperState() {
		return shipperState;
	}

	public void setShipperState(String shipperState) {
		this.shipperState = shipperState;
	}

	public String getShipperCountry() {
		return shipperCountry;
	}

	public void setShipperCountry(String shipperCountry) {
		this.shipperCountry = shipperCountry;
	}

	public String getShipperPostcode() {
		return shipperPostcode;
	}

	public void setShipperPostcode(String shipperPostcode) {
		this.shipperPostcode = shipperPostcode;
	}

	public String getBarcodeLabelNumber() {
		return barcodeLabelNumber;
	}

	public void setBarcodeLabelNumber(String barcodeLabelNumber) {
		this.barcodeLabelNumber = barcodeLabelNumber;
	}

	public String getDatamatrix() {
		return datamatrix;
	}

	public void setDatamatrix(String datamatrix) {
		this.datamatrix = datamatrix;
	}

	@Override
	public String toString() {
		return "SenderData [senderFileId=" + senderFileId + ", referenceNumber=" + referenceNumber + ", consigneeName="
				+ consigneeName + ", consigneeAddr1=" + consigneeAddr1 + ", consigneeSuburb=" + consigneeSuburb
				+ ", consigneeState=" + consigneeState + ", consigneePostcode=" + consigneePostcode
				+ ", consigneePhone=" + consigneePhone + ", weight=" + weight + ", shipperName=" + shipperName
				+ ", shipperAddr1=" + shipperAddr1 + ", shipperAddr2=" + shipperAddr2 + ", shipperCity=" + shipperCity
				+ ", shipperState=" + shipperState + ", shipperCountry=" + shipperCountry + ", shipperPostcode="
				+ shipperPostcode + ", barcodeLabelNumber=" + barcodeLabelNumber + ", datamatrix=" + datamatrix
				+ ", injectionState=" + injectionState + "]";
	}
	
}
