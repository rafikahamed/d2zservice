package com.d2z.d2zservice.model;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SenderDataApi {
	
	private int userID;
	private String senderFilesID;
	@NotEmpty(message = "Reference Number is mandatory")
	//@Pattern(regexp="^[a-zA-Z0-9]+$", message = "Invalid reference number")
	private String referenceNumber;
	//@NotEmpty(message = "Consignee Name is mandatory")
	//@Pattern(regexp="^[a-zA-Z0-9]+$", message = "Consignee Name must contain only alphabets")
	private String consigneeName;
	private String consigneeCompany;
	@NotEmpty(message = "Consignee Address is mandatory")
	@Size(max=50,message = "Consignee Address1 must not exceed 50 characters")	
	private String consigneeAddr1;
	@Size(max=50,message = "Consignee Address2 must not exceed 50 characters")
	private String consigneeAddr2;
	@NotEmpty(message = "Consignee Suburb is mandatory")
	private String consigneeSuburb;
	@NotEmpty(message = "Consignee State is mandatory")
	private String consigneeState;
	@NotEmpty(message = "Consignee Postcode is mandatory")
	private String consigneePostcode;
	private String consigneePhone;
	private String consigneeEmail;

	@NotEmpty(message = "Product Description is mandatory")
	private String productDescription;
	@NotNull(message = "Value is mandatory")
	@Min(value = 1, message = "Value should be grater than 1")
	@Digits( fraction =2, message = "Invalid Value", integer = 10)
	private double value;
	//@NotEmpty(message = "Currency is mandatory")
	private String currency;
	//@Min(value = 1, message = "Shipped Quantity is mandatory")
	private int shippedQuantity;
	
	@Digits( fraction =2, message = "Invalid Weight", integer = 10)
	@NotEmpty(message = "Weight is mandatory")
	@Min(value = 0, message = "Weight is mandatory")
	@Max(value = 22, message = "Weight should not be greater than 22")
	private String weight;
	
	private BigDecimal cubicWeight;
//	@Digits( fraction =2, message = "Invalid Dimensions Length", integer = 10)
	private BigDecimal dimensionsLength;
//	@Digits( fraction =2, message = "Invalid Dimensions Height", integer = 10)
	private BigDecimal dimensionsHeight;
//	@Digits( fraction =2, message = "Invalid Dimensions Width", integer = 10)
	private BigDecimal dimensionsWidth;
	@NotEmpty(message = "Service Type is mandatory")
	//@Pattern(regexp="^[1-5][pP][a-zA-Z]*$", message = "Invalid Service Type")
	private String serviceType;
	private String deliverytype;
//	@NotEmpty(message = "Shipped Name is mandatory")
//	@Pattern(regexp="^[a-zA-Z ]+$", message = "Shipper Name must contain only alphabets")
	private String shipperName;
//	@NotEmpty(message = "Shipped Address is mandatory")
	private String shipperAddr1;
//	@NotEmpty(message = "Shipped City is mandatory")
	private String shipperCity;
//	@NotEmpty(message = "Shipped State is mandatory")
	private String shipperState;
//	@NotEmpty(message = "Shipped Country is mandatory")
	private String shipperCountry;
//	@NotEmpty(message = "Shipped Postcode is mandatory")
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
	private String InjectionType;
	private String bagId;
	private String userName;
	private String sku;
	private String labelSenderName;
	private String deliveryInstructions;
	private String carrier;
	private String zoneID;
	public String getZoneID() {
		return zoneID;
	}

	public void setZoneID(String zoneID) {
		this.zoneID = zoneID;
	}

	public String getConsigneeAddr2() {
		return consigneeAddr2;
	}

	public void setConsigneeAddr2(String consigneeAddr2) {
		this.consigneeAddr2 = consigneeAddr2;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInjectionType() {
		return InjectionType;
	}

	public void setInjectionType(String injectionType) {
		InjectionType = injectionType;
	}

	public String getBagId() {
		return bagId;
	}

	public void setBagId(String bagId) {
		this.bagId = bagId;
	}

	public String getConsigneeCompany() {
		return consigneeCompany;
	}

	public void setConsigneeCompany(String consigneeCompany) {
		this.consigneeCompany = consigneeCompany;
	}

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
		this.consigneeSuburb = consigneeSuburb.trim();
	}

	public String getConsigneeState() {
		return consigneeState;
	}

	public void setConsigneeState(String consigneeState) {
		this.consigneeState = consigneeState.trim();
	}

	public String getConsigneePostcode() {
		return consigneePostcode;
	}

	public void setConsigneePostcode(String consigneePostcode) {
		this.consigneePostcode = consigneePostcode.trim();
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getLabelSenderName() {
		return labelSenderName;
	}

	public void setLabelSenderName(String labelSenderName) {
		this.labelSenderName = labelSenderName;
	}

	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}

	public void setDeliveryInstructions(String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}

	public String getConsigneeEmail() {
		return consigneeEmail;
	}

	public void setConsigneeEmail(String consigneeEmail) {
		this.consigneeEmail = consigneeEmail;
	}



}
