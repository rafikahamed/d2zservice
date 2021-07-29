package com.d2z.d2zservice.model.veloce;

import java.util.List;

import lombok.Data;

@Data
public class VeloceTrackingRequest {

	private RequestHeader hdr;
	private List<Tracking> bdr;
}
