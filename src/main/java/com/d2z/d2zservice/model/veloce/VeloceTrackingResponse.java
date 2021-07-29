package com.d2z.d2zservice.model.veloce;

import java.util.List;

import lombok.Data;

@Data
public class VeloceTrackingResponse {
	
	private ResponseHeader hdr;
	private List<VeloceTrackResponse> bdr;

}
