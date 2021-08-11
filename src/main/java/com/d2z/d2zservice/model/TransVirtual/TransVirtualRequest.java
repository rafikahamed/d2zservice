package com.d2z.d2zservice.model.TransVirtual;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TransVirtualRequest {

	@JsonProperty("UniqueId")
	private String UniqueId;
	@JsonProperty("Number")
	private String Number;
	@JsonProperty("Date")
	private String Date;
	@JsonProperty("CustomerCode")
	private String CustomerCode;
	@JsonProperty("SenderName")
	private String SenderName;
	@JsonProperty("SenderAddress")
	private String SenderAddress;
	@JsonProperty("SenderAddress2")
	private String SenderAddress2;
	@JsonProperty("SenderSuburb")
	private String SenderSuburb;
	@JsonProperty("SenderPostcode")
	private String SenderPostcode;
	@JsonProperty("SenderState")
	private String SenderState;
	@JsonProperty("SenderReference")
	private String SenderReference;
	@JsonProperty("ConsignmentSenderContact")
	private String ConsignmentSenderContact;
	@JsonProperty("ConsignmentSenderPhone")
	private String ConsignmentSenderPhone;
	@JsonProperty("ReceiverEmail")
	private String ReceiverEmail;
	@JsonProperty("SenderEmail")
	private String SenderEmail;
	@JsonProperty("ReceiverName")
	private String ReceiverName;

	@JsonProperty("ReceiverAddress")
	private String ReceiverAddress;
	@JsonProperty("ReceiverSuburb")
	private String ReceiverSuburb;
	@JsonProperty("ReceiverPostcode")
	private String ReceiverPostcode;
	@JsonProperty("ReceiverState")
	private String ReceiverState;
	@JsonProperty("SpecialInstructions")
	private String SpecialInstructions;
	@JsonProperty("ConsignmentReceiverContact")
	private String ConsignmentReceiverContact;
	@JsonProperty("ConsignmentReceiverPhone")
	private String ConsignmentReceiverPhone;
	@JsonProperty("AutoAssignAgentEmployee")
	private String AutoAssignAgentEmployee;
	@JsonProperty("PickupRequest")
	private String PickupRequest;
	@JsonProperty("Rows")
	private List<Rows> Rows;
	
	
}
