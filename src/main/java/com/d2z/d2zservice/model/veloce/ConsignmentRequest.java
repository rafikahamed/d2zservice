package com.d2z.d2zservice.model.veloce;

import java.util.List;

import lombok.Data;

@Data
public class ConsignmentRequest {
	
	private RequestHeader hdr;
	private List<Consignment> bdr;
	
}
