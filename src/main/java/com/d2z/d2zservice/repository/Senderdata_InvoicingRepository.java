package com.d2z.d2zservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.d2z.d2zservice.entity.Senderdata_Invoicing;

public interface Senderdata_InvoicingRepository extends CrudRepository<Senderdata_Invoicing, Long>{
	
	@Procedure(name = "InvoiceUpdate")
	void approvedInvoice(@Param("Indicator") String Indicator, @Param("Airwaybill") String Airwaybill);
	
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query(nativeQuery = true, value="Update Senderdata_Invoicing  set Weight = :weight,Invoiced = 'N', Billed = 'N',d2zrate=null,brokerrate =null  where ArticleId = :articleid")
	void updateinvoicingweight(@Param("weight") Double weight, @Param("articleid") String articleid);

	
	@Modifying(flushAutomatically = true,clearAutomatically = true)
	@Transactional
	@Query( nativeQuery = true ,value="Update Senderdata_Invoicing   set airwayBill = :airwaybill,invoiced = 'N', billed ='N'  where articleId in (:articleid)")
	int updateinvoicingairway(@Param("airwaybill") String airway, @Param("articleid") String[] articleid);
	                                                                                                         
	
	@Query( nativeQuery = true ,value="Select count(*) from Senderdata_Invoicing    where articleId in (:articleid)")
	int selectinvoicing ( @Param("articleid") String[]  articleid);
}
