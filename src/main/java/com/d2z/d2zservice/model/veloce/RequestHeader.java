package com.d2z.d2zservice.model.veloce;

import lombok.Data;

@Data
public class RequestHeader {
	private String client_id;
	private String request_date;
	private String request_sign;	
}
