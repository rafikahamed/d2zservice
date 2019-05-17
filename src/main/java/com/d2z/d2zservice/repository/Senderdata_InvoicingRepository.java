package com.d2z.d2zservice.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.d2z.d2zservice.entity.Senderdata_Invoicing;

public interface Senderdata_InvoicingRepository extends CrudRepository<Senderdata_Invoicing, Long>{
	
	@Procedure(name = "InvoiceUpdate")
	void approvedInvoice(@Param("Indicator") String Indicator, @Param("Airwaybill") String Airwaybill);

}
