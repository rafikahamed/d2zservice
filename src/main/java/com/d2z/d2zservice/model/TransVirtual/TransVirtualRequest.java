package com.d2z.d2zservice.model.TransVirtual;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransVirtualRequest {
	
	private String UniqueId = "87283242837426";
	private String Number = "HA88332232343";
	private String Date ="2021-03-30";
	private String SenderName = "John";
	private String SenderAddress="PO Box 4211";
	private String SenderAddress2 = "addr2";
	private String SenderSuburb ="McKinnon";
	private String SenderPostcode = "3204";
	private String SenderState= "VIC";
	private String ReceiverName="Emma";
	private String ReceiverAddress="4 O’Connors Road";
	private String ReceiverSuburb="The patch";
	private String ReceiverPostcode="3792";
	private String ReceiverState="VIC";
	private String SpecialInstructions="Handle with care";
	private String ConsignmentReceiverContact="Emma";
	private String ConsignmentReceiverPhone="8824782637";
	private List<Rows> Rows;

	@JsonProperty("UniqueId")
	public String getUniqueId() {
		return UniqueId;
	}

	public void setUniqueId(String uniqueId) {
		UniqueId = uniqueId;
	}

	@JsonProperty("Number")
	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}
	@JsonProperty("Date")
	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	@JsonProperty("SenderName")
	public String getSenderName() {
		return SenderName;
	}

	public void setSenderName(String senderName) {
		SenderName = senderName;
	}
	
	@JsonProperty("SenderAddress")
	public String getSenderAddress() {
		return SenderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		SenderAddress = senderAddress;
	}
	@JsonProperty("SenderAddress2")
	public String getSenderAddress2() {
		return SenderAddress2;
	}

	public void setSenderAddress2(String senderAddress2) {
		SenderAddress2 = senderAddress2;
	}

	@JsonProperty("SenderSuburb")
	public String getSenderSuburb() {
		return SenderSuburb;
	}

	public void setSenderSuburb(String senderSuburb) {
		SenderSuburb = senderSuburb;
	}

	@JsonProperty("SenderPostcode")
	public String getSenderPostcode() {
		return SenderPostcode;
	}

	public void setSenderPostcode(String senderPostcode) {
		SenderPostcode = senderPostcode;
	}

	@JsonProperty("SenderState")
	public String getSenderState() {
		return SenderState;
	}

	public void setSenderState(String senderState) {
		SenderState = senderState;
	}
	@JsonProperty("ReceiverName")
	public String getReceiverName() {
		return ReceiverName;
	}

	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}

	@JsonProperty("ReceiverAddress")
	public String getReceiverAddress() {
		return ReceiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		ReceiverAddress = receiverAddress;
	}

	@JsonProperty("ReceiverSuburb")
	public String getReceiverSuburb() {
		return ReceiverSuburb;
	}

	public void setReceiverSuburb(String receiverSuburb) {
		ReceiverSuburb = receiverSuburb;
	}

	@JsonProperty("ReceiverPostcode")
	public String getReceiverPostcode() {
		return ReceiverPostcode;
	}

	public void setReceiverPostcode(String receiverPostcode) {
		ReceiverPostcode = receiverPostcode;
	}

	@JsonProperty("ReceiverState")
	public String getReceiverState() {
		return ReceiverState;
	}

	public void setReceiverState(String receiverState) {
		ReceiverState = receiverState;
	}

	@JsonProperty("SpecialInstructions")
	public String getSpecialInstructions() {
		return SpecialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		SpecialInstructions = specialInstructions;
	}
	@JsonProperty("ConsignmentReceiverContact")
	public String getConsignmentReceiverContact() {
		return ConsignmentReceiverContact;
	}

	public void setConsignmentReceiverContact(String consignmentReceiverContact) {
		ConsignmentReceiverContact = consignmentReceiverContact;
	}
	@JsonProperty("ConsignmentReceiverPhone")
	public String getConsignmentReceiverPhone() {
		return ConsignmentReceiverPhone;
	}

	public void setConsignmentReceiverPhone(String consignmentReceiverPhone) {
		ConsignmentReceiverPhone = consignmentReceiverPhone;
	}

	@JsonProperty("Rows")
	public List<Rows> getRows() {
		if(Rows==null) {
			return new ArrayList<Rows>();
		}
		return Rows;
	}

	public void setRows(List<Rows> rows) {
		Rows = rows;
	}
	
	
}
