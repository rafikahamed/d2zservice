package com.d2z.d2zservice.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsignmentDTO {
	private String supplierAuthId;
	private String referenceNumber;
	private String airwayBill;
	private String bagId;
	private String barcodelabelNumber;
	private String consigneeAddr1;
	private String consigneeName;
	private String consigneePhone;
	private String consigneePostcode;
	private String consigneeState;
	private String consigneeSuburb;
	private String consigneeCompany;
	private String consigneeAddr2;
	private String consigneeEmail;
	private BigDecimal cubicWeight;
	private String currency;
	private String customsStatus;
	private String datamatrix;
	private String deliverytype;
	private BigDecimal dimensionsHeight;
	private BigDecimal dimensionsLength;
	private BigDecimal dimensionsWidth;
	private String filename;
	private String injectionState;
	private String injectionType;
	private int innerItem;
	private String isDeleted;
	private String manifestNumber;
	private String productDescription;
	private String senderFilesID;
	private String servicetype;
	private int shippedQuantity;
	private String shipperAddr1;
	private String shipperCity;
	private String shipperCountry;
	private String shipperName;
	private String shipperPostcode;
	private String shipperState;
	private String shipperCompany;
	private String status;
	private String timestamp;
	private int userID;
	private double value;
	private double weight;
	private String sku;
	private String labelSenderName;
	private String deliveryInstructions;
	private String articleId;
	private String carrier;
	private String mlid;
	private BigDecimal fuelSurcharge;
	private String invoiced;
	private String billed;
	private String returnAddress1;
	private String returnAddress2;
	private String d2zRate;
	private String brokerRate;
	private String vendorId;
	private String custReference;
	private String courier;
}
