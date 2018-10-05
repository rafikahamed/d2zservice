package com.d2z.d2zservice.model;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class SenderData {	
	
	private int userID;
	private String senderFilesID;
	private String referenceNumber;
	@Pattern(regexp="^[a-zA-Z ]+$", message = "Consignee Name must contain only alphabets")
	private String consigneeName;
	private String consigneeAddr1;
	@NotEmpty(message = "Consignee Suburb is mandatory")
	private String consigneeSuburb;
	private String consigneeState;
	@NotEmpty(message = "Consignee Postcode is mandatory")
	private String consigneePostcode;
	private String consigneePhone;
	private String productDescription;
	@Digits( fraction =2, message = "Invalid Value", integer = 10)
	private double value;
	private String currency;
	private int shippedQuantity;
	@Digits( fraction =2, message = "Invalid Weight", integer = 10)
	private String weight;
	private BigDecimal cubicWeight;
	@Digits( fraction =2, message = "Invalid Dimensions Length", integer = 10)
	private BigDecimal dimensionsLength;
	@Digits( fraction =2, message = "Invalid Dimensions Height", integer = 10)
	private BigDecimal dimensionsHeight;
	@Digits( fraction =2, message = "Invalid Dimensions Width", integer = 10)
	private BigDecimal dimensionsWidth;
	@Pattern(regexp="^[1-5][pP][a-zA-Z]*$", message = "Invalid Service Type")
	private String serviceType;
	private String deliverytype;
	@Pattern(regexp="^[a-zA-Z ]+$", message = "Shipper Name must contain only alphabets")
	private String shipperName;
	private String shipperAddr1;
	private String shipperAddr2;
	private String shipperCity;
	private String shipperState;
	private String shipperCountry;
	private String shipperPostcode;
	private String barcodeLabelNumber;
	private String datamatrix;		
	private String customsStatus;
	private String manifestNumber;
	private String airwayBill;
	private String palletId;
	private String fileName;
	private String status;
	private String isDeleted;
	private int rowId;
	private Timestamp timestamp;
	private String injectionState;
	private BufferedImage datamatrixImage;

	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getSenderFilesID() {
		return senderFilesID;
	}

	public void setSenderFilesID(String senderFilesID) {
		this.senderFilesID = senderFilesID;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(int shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public BigDecimal getCubicWeight() {
		return cubicWeight;
	}

	public void setCubicWeight(BigDecimal cubicWeight) {
		this.cubicWeight = cubicWeight;
	}

	public BigDecimal getDimensionsLength() {
		return dimensionsLength;
	}

	public void setDimensionsLength(BigDecimal dimensionsLength) {
		this.dimensionsLength = dimensionsLength;
	}

	public BigDecimal getDimensionsHeight() {
		return dimensionsHeight;
	}

	public void setDimensionsHeight(BigDecimal dimensionsHeight) {
		this.dimensionsHeight = dimensionsHeight;
	}

	public BigDecimal getDimensionsWidth() {
		return dimensionsWidth;
	}

	public void setDimensionsWidth(BigDecimal dimensionsWidth) {
		this.dimensionsWidth = dimensionsWidth;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDeliverytype() {
		return deliverytype;
	}

	public void setDeliverytype(String deliverytype) {
		this.deliverytype = deliverytype;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getManifestNumber() {
		return manifestNumber;
	}

	public void setManifestNumber(String manifestNumber) {
		this.manifestNumber = manifestNumber;
	}

	public String getAirwayBill() {
		return airwayBill;
	}

	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
	}

	public String getPalletId() {
		return palletId;
	}

	public void setPalletId(String palletId) {
		this.palletId = palletId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

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
		return "SenderData [referenceNumber=" + referenceNumber + ", consigneeName="
				+ consigneeName + ", consigneeAddr1=" + consigneeAddr1 + ", consigneeSuburb=" + consigneeSuburb
				+ ", consigneeState=" + consigneeState + ", consigneePostcode=" + consigneePostcode
				+ ", consigneePhone=" + consigneePhone + ", weight=" + weight + ", shipperName=" + shipperName
				+ ", shipperAddr1=" + shipperAddr1 + ", shipperAddr2=" + shipperAddr2 + ", shipperCity=" + shipperCity
				+ ", shipperState=" + shipperState + ", shipperCountry=" + shipperCountry + ", shipperPostcode="
				+ shipperPostcode + ", barcodeLabelNumber=" + barcodeLabelNumber + ", datamatrix=" + datamatrix
				+ ", injectionState=" + injectionState + "]";
	}
	
}
