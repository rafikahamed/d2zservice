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
	@Query( nativeQuery = true ,value="Update Senderdata_Invoicing set airwayBill = :airwaybill,invoiced = 'N', billed ='N' where articleId in (:articleid)")
	int updateinvoicingairway(@Param("airwaybill") String airway, @Param("articleid") String[] articleid);
	
	@Query( nativeQuery = true ,value="Select count(*) from Senderdata_Invoicing where articleId in (:articleid)")
	int selectinvoicing ( @Param("articleid") String[]  articleid);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true ,value="update senderdata_invoicing set airwayBill = :airwayBill, invoiced = 'N', billed = 'N', d2zRate = NULL, brokerRate = NULL, fuelSurcharge = NULL, servicetype = :servicetype where articleId = :articleId")
	void updateReturnInvoice(@Param("airwayBill") String airwayBill, @Param("servicetype") String servicetype,  @Param("articleId") String articleId);
	
	@Query(nativeQuery = true ,value="with detl(zone, category, count_val) as \r\n" + 
			"(select zone as zone, category, count(category) as count_val \r\n" + 
			"from\r\n" + 
			"(select Reference_number, Weight as weight, zone as zone,\r\n" + 
			"	case when Weight between 0.00 and 0.50 THEN 'category1'\r\n" + 
			"		 when Weight between 0.51 and 1.00 THEN 'category2'\r\n" + 
			"		 when Weight between 1.01 and 2.00 THEN 'category3'\r\n" + 
			"	     when Weight between 2.01 and 3.00 THEN 'category4'\r\n" + 
			"	     when Weight between 3.01 and 4.00 THEN 'category5'\r\n" + 
			"		 when Weight between 4.01 and 5.00 THEN 'category6'\r\n" + 
			"		 when Weight between 5.01 and 7.00 THEN 'category7'\r\n" + 
			"		 when Weight between 7.01 and 10.00 THEN 'category8'\r\n" + 
			"		 when Weight between 10.01 and 15.00 THEN 'category9'\r\n" + 
			"		 when Weight between 15.01 and 22.00 THEN 'category10'	 \r\n" + 
			"	 END as category\r\n" + 
			"FROM [D2Z].[dbo].[Senderdata_Invoicing] \r\n" + 
			"where user_id IN (:userId) and DateAllocated between \r\n" + 
			" convert(datetime, :fromDate, 121)\r\n" + 
			"  and \r\n" + 
			" convert(datetime, :toDate, 121)\r\n" + 
			"and zone is not null ) tbl\r\n" + 
			"group by zone,category),\r\n" + 
			"ttl(zone,t_cnt) as \r\n" + 
			"(select zone, sum(count_val) as t_cnt \r\n" + 
			"from (select * from detl) tbl3 group by zone),\r\n" + 
			"section1(zone, category,count_val, t_cnt) as \r\n" + 
			"(select ttl.zone as zone, category,count_val, t_cnt \r\n" + 
			"from \r\n" + 
			"ttl\r\n" + 
			"inner join\r\n" + 
			"detl\r\n" + 
			"on ttl.zone=detl.zone),\r\n" + 
			"section2(sum_val) as\r\n" + 
			"(select sum(t_cnt) as sum_val  from ttl),\r\n" + 
			"section_category(category,sum_cat) as\r\n" + 
			"(select category,sum(count_val) as sum_cat from detl group by category)\r\n" + 
			"select summation.*,cat_result.sum_cat,(cast(cat_result.sum_cat as float)/ cast(sum_val as float))*100 as perc_cat \r\n" + 
			"from\r\n" + 
			"(select zone,category,count_val,t_cnt,sum_val, (cast(t_cnt as float)/ cast(sum_val as float))*100 as perc_zone \r\n" + 
			"from section1, section2)summation\r\n" + 
			"inner join\r\n" + 
			"(select * from section_category)cat_result\r\n" + 
			"on summation.category=cat_result.category\r\n" + 
			"order by zone,category")
	List<String>zoneReport(@Param("userId") List<Integer> userId, @Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	@Query(nativeQuery = true ,value="SELECT \r\n" + 
			"DISTINCT \r\n" + 
			"				S. brokerusername    AS BrokerName, \r\n" + 
			"                S.articleid          AS TrackingNumber, \r\n" + 
			"                S.reference_number   AS reference, \r\n" + 
			"                S.consignee_postcode AS postcode, \r\n" + 
			"                S.weight             AS Weight, \r\n" + 
			"                S.postage            AS postage, \r\n" + 
			"                S.fuelsurcharge      AS Fuelsurcharge, \r\n" + 
			"                S.brokerrate         AS total, \r\n" + 
			"                S.servicetype        AS servicetype, \r\n" + 
			"                S.airwaybill         AS ShipmentNumber, \r\n" +
			"				 S.Consignee_Suburb  AS Suburb, \r\n"+
			"				 S.Zone  AS Zone \r\n"+
			"FROM   [D2Z].[dbo].[senderdata_invoicing] S \r\n" + 
			"WHERE  S.airwaybill IN ( :airwayBill ) \r\n" + 
			"       AND S.brokerusername IN ( :broker ) \r\n" + 
			"       AND S.billed = :billed \r\n" + 
			"       AND S.invoiced = :invoiced")
	List<String> downloadInvoice(@Param("broker") List<String> broker, @Param("airwayBill")  List<String> airwayBill, 
											@Param("billed") String billed, @Param("invoiced") String invoiced);
	
	@Query(nativeQuery = true ,value="select\r\n" + 
			"Brokerusername,\r\n" + 
			"count(Weight) as parcel,\r\n" + 
			"sum(BrokerRate) as brokerRate,\r\n" + 
			"sum(D2ZRate) as D2ZRate\r\n" + 
			"  FROM [D2Z].[dbo].[Senderdata_Invoicing]\r\n" + 
			"  where\r\n" + 
			"  DateAllocated between :fromDate and :toDate\r\n" + 
			"  and brokerRate is not null and D2ZRate is not null\r\n" + 
			"  and Brokerusername not in ('5ULB')\r\n" + 
			"  group by Brokerusername\r\n" + 
			"\r\n" + 
			"union\r\n" + 
			"\r\n" + 
			"select\r\n" + 
			"'5ULB HKG' as Brokerusername,\r\n" + 
			"count(Weight) as parcel,\r\n" + 
			"sum(BrokerRate) as brokerRate,\r\n" + 
			"sum(D2ZRate) as D2ZRate\r\n" + 
			"  FROM [D2Z].[dbo].[Senderdata_Invoicing]\r\n" + 
			"  where\r\n" + 
			"  DateAllocated between :fromDate and :toDate \r\n" + 
			"  and brokerRate is not null and D2ZRate is not null\r\n" + 
			"  and Brokerusername = '5ULB'\r\n" + 
			"  and Servicetype in ('HKG','HKG2')\r\n" + 
			"  group by Brokerusername\r\n" + 
			"\r\n" + 
			"union\r\n" + 
			"\r\n" + 
			"select\r\n" + 
			"'5ULB Other' as Brokerusername,\r\n" + 
			"count(Weight) as parcel,\r\n" + 
			"sum(BrokerRate) as brokerRate,\r\n" + 
			"sum(D2ZRate) as D2ZRate\r\n" + 
			"  FROM [D2Z].[dbo].[Senderdata_Invoicing]\r\n" + 
			"  where\r\n" + 
			"  DateAllocated between :fromDate and :toDate \r\n" + 
			"  and brokerRate is not null and D2ZRate is not null\r\n" + 
			"  and Brokerusername = '5ULB'\r\n" + 
			"  and Servicetype not in ('HKG','HKG2')\r\n" + 
			"  group by Brokerusername;")
	List<String> getBrokerProfitDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	@Query(value="select s FROM Senderdata_Invoicing s where s.dateAllocated between :fromDate and :toDate and s.brokerRate is not null and s.d2zRate is not null")
	List<Senderdata_Invoicing> getSupplierDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	@Query(nativeQuery = true ,value="select\r\n" + 
			"'APG' as Brokerusername,\r\n" + 
			"count(Weight) as parcel,\r\n" + 
			"sum(BrokerRate) as brokerRate,\r\n" + 
			"sum(D2ZRate) as D2ZRate\r\n" + 
			"  FROM [D2Z].[dbo].[Senderdata_Invoicing]\r\n" + 
			"  where\r\n" + 
			"  DateAllocated between :fromDate and :toDate and Servicetype in ('HKG','HKG2') and brokerRate is not null and D2ZRate is not null \r\n" + 
			"  group by Brokerusername")
	List<String> getApgBrokerProfitDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query("Select s from Senderdata_Invoicing s where s.articleId = :trackingNumber and s.uploadType = 'Manual'")
	Senderdata_Invoicing fetchByArticleId(String trackingNumber);
	
}
