package com.d2z.d2zservice.model.veloce;

import lombok.Data;

@Data
public class Consignment {
	
	private String courier;
	private String currency="";
	private String height="";
	private String item_desc="";
	private String length="";
	private String other_ref="";
	private String recipient_address1="";
	private String recipient_address2="";
	private String recipient_address3 = "";
	private String recipient_city="";
	private String recipient_country="";
	private String recipient_email="";
	private String recipient_name="";
	private String recipient_phone="";
	private String recipient_postcode="";
	private String recipient_state_name="";
	private String remark="";
	private String shipper_address1="";
	private String shipper_address2="";
	private String shipper_address3="";
	private String shipper_city="";
	private String shipper_country="";
	private String shipper_email="";
	private String shipper_name="";
	private String shipper_phone="";
	private String shipper_postcode="";
	private String shipper_ref="";
	private String shipper_state_name="";
	private String tracking_id="";
	private String value="";
	private String warehouse_code="";
	private String weight="";
	private String width="";
}
