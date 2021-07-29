package com.d2z.d2zservice.model;

import lombok.Data;

@Data
public class TrackingRequest {
	private String barcodelabelNumber;
	private String fileName;
	private String isDeleted;
	private String isDelivered;
	private String reference_number;
	private String createTimestamp;
	private String modifiedTimestamp;
	private int user_Id;
	private String articleID;
	private String serviceType;
	private String carrier;

}
