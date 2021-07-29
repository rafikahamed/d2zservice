package com.d2z.d2zservice.model;

import java.util.List;
import java.util.Map;

import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.SupplierEntity;

import lombok.Data;

@Data
public class ConsignmentConfig {

	private int userId;
	private boolean isPostCodeValidationRequired;
	private boolean autoShipmentIndicator;
	private String serviceType;
	private Map<SupplierEntity,List<ConsignmentDTO>> supplierConsignmentMap;
	

	
}
