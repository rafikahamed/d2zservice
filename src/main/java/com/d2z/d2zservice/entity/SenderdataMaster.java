package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * The persistent class for the SENDERDATA_MASTER database table.
 * 
 */
@Entity
@Table(name="SENDERDATA_MASTER",  schema="dbo")
@NamedQuery(name="SenderdataMaster.findAll", query="SELECT s FROM SenderdataMaster s")
@NamedStoredProcedureQueries({
   @NamedStoredProcedureQuery(name = "in_only_test", 
                              procedureName = "GenerateBarCodeLabelNumber",
                              parameters = {
                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "Sender_file_id", type = String.class)
                              }),
   @NamedStoredProcedureQuery(name = "consignee_delete", 
							  procedureName = "deleteConsignment",
							  parameters = {
							      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Reference_number", type = String.class)
							  }),
   @NamedStoredProcedureQuery(name = "manifest_creation", 
	  procedureName = "ManifestAllocation",
	  parameters = {
	      @StoredProcedureParameter(mode = ParameterMode.IN, name = "ManifestNumber", type = String.class),
	      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Reference_number", type = String.class)
	  }),
   @NamedStoredProcedureQuery(name = "shipment_allocation", 
	  procedureName = "ShipmentAllocation",
	  parameters = {
	      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Airwaybill", type = String.class),
	      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Reference_number", type = String.class)
	  }),
   @NamedStoredProcedureQuery(name = "deleteConsignment", 
	  procedureName = "deleteConsignment",
	  parameters = {
	      @StoredProcedureParameter(mode = ParameterMode.IN, name = "Reference_number", type = String.class)
	  })
})
public class SenderdataMaster implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="Reference_number")
	private String reference_number;

	@Column(name="AirwayBill")
	private String airwayBill;

	@Column(name="BagId")
	private String bagId;

	@Column(name="BarcodelabelNumber")
	private String barcodelabelNumber;

	@Column(name="Consignee_addr1")
	private String consignee_addr1;

	@Column(name="Consignee_addr2")
	private String consignee_addr2;

	@Column(name="Consignee_name")
	private String consignee_name;

	@Column(name="Consignee_Phone")
	private String consignee_Phone;

	@Column(name="Consignee_Postcode")
	private String consignee_Postcode;

	@Column(name="Consignee_State")
	private String consignee_State;

	@Column(name="Consignee_Suburb")
	private String consignee_Suburb;

	@Column(name="ConsigneeCompany")
	private String consigneeCompany;
	
	@Column(name="Consignee_addr2")
	private String consignee_addr2;

	@Column(name="Consignee_Email")
	private String consignee_Email;

	@Column(name="Cubic_Weight")
	private BigDecimal cubic_Weight;

	@Column(name="Currency")
	private String currency;

	@Column(name="CustomsStatus")
	private String customsStatus;

	@Column(name="Datamatrix")
	private String datamatrix;

	@Column(name="Deliverytype")
	private String deliverytype;

	@Column(name="Dimensions_Height")
	private BigDecimal dimensions_Height;

	@Column(name="Dimensions_Length")
	private BigDecimal dimensions_Length;

	@Column(name="Dimensions_Width")
	private BigDecimal dimensions_Width;

	@Column(name="Filename")
	private String filename;

	@Column(name="InjectionState")
	private String injectionState;

	@Column(name="InjectionType")
	private String injectionType;

	@Column(name="InnerItem")
	private int innerItem;

	@Column(name="IsDeleted")
	private String isDeleted;

	@Column(name="Manifest_number")
	private String manifest_number;

	@Column(name="Product_Description")
	private String product_Description;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "RowIDSeqNum", allocationSize = 10) 
	@Column(name="row_id")
	private int rowId;

	@Column(name="Sender_Files_ID")
	private String sender_Files_ID;

	@Column(name="Servicetype")
	private String servicetype;

	@Column(name="shipped_quantity")
	private int shippedQuantity;

	@Column(name="Shipper_Addr1")
	private String shipper_Addr1;

	@Column(name="Shipper_City")
	private String shipper_City;

	@Column(name="Shipper_Country")
	private String shipper_Country;

	@Column(name="Shipper_Name")
	private String shipper_Name;

	@Column(name="Shipper_Postcode")
	private String shipper_Postcode;

	@Column(name="Shipper_State")
	private String shipper_State;

	@Column(name="ShipperCompany")
	private String shipperCompany;

	@Column(name="Status")
	private String status;

	@Column(name="Timestamp")
	private String timestamp;

	@Column(name="User_ID")
	private int user_ID;

	private double value;

	@Column(name="Weight")
	private double weight;
	
	@Column(name="SKU")
	private String sku;
	
	@Column(name="LabelSenderName")
	private String labelSenderName;
	
	@Column(name="DeliveryInstructions")
	private String deliveryInstructions;
	
	@Column(name="ArticleId")
	private String articleId;
	
	@Column(name="Carrier")
	private String carrier;
	
	
	public String getConsignee_addr2() {
		return consignee_addr2;
	}

	public void setConsignee_addr2(String consignee_addr2) {
		this.consignee_addr2 = consignee_addr2;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	@OneToMany(mappedBy="senderData")
    List<Trackandtrace> trackAndTrace = null;
	
	public String getConsigneeCompany() {
		return consigneeCompany;
	}

	public void setConsigneeCompany(String consigneeCompany) {
		this.consigneeCompany = consigneeCompany;
	}

	public SenderdataMaster() {
	}

	public String getReference_number() {
		return this.reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getAirwayBill() {
		return this.airwayBill;
	}

	public void setAirwayBill(String airwayBill) {
		this.airwayBill = airwayBill;
	}

	public String getBagId() {
		return this.bagId;
	}

	public void setBagId(String bagId) {
		this.bagId = bagId;
	}

	public String getBarcodelabelNumber() {
		return this.barcodelabelNumber;
	}

	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}

	public String getConsignee_addr1() {
		return this.consignee_addr1;
	}

	public void setConsignee_addr1(String consignee_addr1) {
		this.consignee_addr1 = consignee_addr1;
	}

	public String getConsignee_name() {
		return this.consignee_name;
	}

	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	public String getConsignee_Phone() {
		return this.consignee_Phone;
	}

	public void setConsignee_Phone(String consignee_Phone) {
		this.consignee_Phone = consignee_Phone;
	}

	public String getConsignee_Postcode() {
		return this.consignee_Postcode;
	}

	public void setConsignee_Postcode(String consignee_Postcode) {
		this.consignee_Postcode = consignee_Postcode;
	}

	public String getConsignee_State() {
		return this.consignee_State;
	}

	public void setConsignee_State(String consignee_State) {
		this.consignee_State = consignee_State;
	}

	public String getConsignee_Suburb() {
		return this.consignee_Suburb;
	}

	public void setConsignee_Suburb(String consignee_Suburb) {
		this.consignee_Suburb = consignee_Suburb;
	}

	public BigDecimal getCubic_Weight() {
		return this.cubic_Weight;
	}

	public void setCubic_Weight(BigDecimal cubic_Weight) {
		this.cubic_Weight = cubic_Weight;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCustomsStatus() {
		return this.customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getDatamatrix() {
		return this.datamatrix;
	}

	public void setDatamatrix(String datamatrix) {
		this.datamatrix = datamatrix;
	}

	public String getDeliverytype() {
		return this.deliverytype;
	}

	public void setDeliverytype(String deliverytype) {
		this.deliverytype = deliverytype;
	}

	public BigDecimal getDimensions_Height() {
		return this.dimensions_Height;
	}

	public void setDimensions_Height(BigDecimal dimensions_Height) {
		this.dimensions_Height = dimensions_Height;
	}

	public BigDecimal getDimensions_Length() {
		return this.dimensions_Length;
	}

	public void setDimensions_Length(BigDecimal dimensions_Length) {
		this.dimensions_Length = dimensions_Length;
	}

	public BigDecimal getDimensions_Width() {
		return this.dimensions_Width;
	}

	public void setDimensions_Width(BigDecimal dimensions_Width) {
		this.dimensions_Width = dimensions_Width;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getInjectionState() {
		return this.injectionState;
	}

	public void setInjectionState(String injectionState) {
		this.injectionState = injectionState;
	}

	public String getInjectionType() {
		return this.injectionType;
	}

	public void setInjectionType(String injectionType) {
		this.injectionType = injectionType;
	}

	public int getInnerItem() {
		return this.innerItem;
	}

	public void setInnerItem(int innerItem) {
		this.innerItem = innerItem;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getManifest_number() {
		return this.manifest_number;
	}

	public void setManifest_number(String manifest_number) {
		this.manifest_number = manifest_number;
	}

	public String getProduct_Description() {
		return this.product_Description;
	}

	public void setProduct_Description(String product_Description) {
		this.product_Description = product_Description;
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getSender_Files_ID() {
		return this.sender_Files_ID;
	}

	public void setSender_Files_ID(String sender_Files_ID) {
		this.sender_Files_ID = sender_Files_ID;
	}

	public String getServicetype() {
		return this.servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public int getShippedQuantity() {
		return this.shippedQuantity;
	}

	public void setShippedQuantity(int shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public String getShipper_Addr1() {
		return this.shipper_Addr1;
	}

	public void setShipper_Addr1(String shipper_Addr1) {
		this.shipper_Addr1 = shipper_Addr1;
	}

	public String getShipper_City() {
		return this.shipper_City;
	}

	public void setShipper_City(String shipper_City) {
		this.shipper_City = shipper_City;
	}

	public String getShipper_Country() {
		return this.shipper_Country;
	}

	public void setShipper_Country(String shipper_Country) {
		this.shipper_Country = shipper_Country;
	}

	public String getShipper_Name() {
		return this.shipper_Name;
	}

	public void setShipper_Name(String shipper_Name) {
		this.shipper_Name = shipper_Name;
	}

	public String getShipper_Postcode() {
		return this.shipper_Postcode;
	}

	public void setShipper_Postcode(String shipper_Postcode) {
		this.shipper_Postcode = shipper_Postcode;
	}

	public String getShipper_State() {
		return this.shipper_State;
	}

	public void setShipper_State(String shipper_State) {
		this.shipper_State = shipper_State;
	}

	public String getShipperCompany() {
		return this.shipperCompany;
	}

	public void setShipperCompany(String shipperCompany) {
		this.shipperCompany = shipperCompany;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getUser_ID() {
		return this.user_ID;
	}

	public void setUser_ID(int user_ID) {
		this.user_ID = user_ID;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getLabelSenderName() {
		return labelSenderName;
	}

	public void setLabelSenderName(String labelSenderName) {
		this.labelSenderName = labelSenderName;
	}

	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}

	public void setDeliveryInstructions(String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getConsignee_addr2() {
		return consignee_addr2;
	}

	public void setConsignee_addr2(String consignee_addr2) {
		this.consignee_addr2 = consignee_addr2;
	}

	public String getConsignee_Email() {
		return consignee_Email;
	}

	public void setConsignee_Email(String consignee_Email) {
		this.consignee_Email = consignee_Email;
	}
	
}