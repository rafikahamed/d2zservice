package com.d2z.d2zservice.model.veloce;

import java.util.List;

import lombok.Data;

@Data
public class VeloceTrackResponse {
	
	private List<DeliveryStatus> delivery_status;
	private String tracking_id;

}
