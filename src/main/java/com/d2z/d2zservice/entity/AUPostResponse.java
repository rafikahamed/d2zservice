package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUPostResponse")
public class AUPostResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RoWID")
	private int rowId;*/
	
	@Column(name = "APIName")
	private String apiname;
	
	@Column(name = "OrderId")
	private String orderid;
	
	@Column(name = "OrderReference")
	private String orderreference;
	
	@Column(name = "OrderCreationDate")
	private String OrderCreationDate;
	
	@Column(name = "ShipmentId")
	private String shipmentId;
	
	@Column(name = "ItemId")
	private String ItemId;
	
	@Id
	@Column(name = "ArticleId")
	private String ArticleId;
	
	@Column(name = "Cost")
	private String Cost;
	
	@Column(name = "GST")
	private String GST;
	
	@Column(name = "FuelSurcharge")
	private String FuelSurcharge;
	
	@Column(name = "ErrorCode")
	private String ErrorCode;
	
	@Column(name = "Name")
	private String Name;
	
	@Column(name = "Message")
	private String Message;
	
	@Column(name = "Field")
	private String Field;
	
	@Column(name = "Timestamp")
	private Timestamp timestamp;

	

	public String getApiname() {
		return apiname;
	}

	public void setApiname(String apiname) {
		this.apiname = apiname;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public void setGST(String gST) {
		GST = gST;
	}

	public String getOrderreference() {
		return orderreference;
	}

	public void setOrderreference(String orderreference) {
		this.orderreference = orderreference;
	}

	public String getOrderCreationDate() {
		return OrderCreationDate;
	}

	public void setOrderCreationDate(String orderCreationDate) {
		OrderCreationDate = orderCreationDate;
	}

	public String getGST() {
		return GST;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getItemId() {
		return ItemId;
	}

	public void setItemId(String itemId) {
		ItemId = itemId;
	}

	public String getArticleId() {
		return ArticleId;
	}

	public void setArticleId(String articleId) {
		ArticleId = articleId;
	}

	public String getCost() {
		return Cost;
	}

	public void setCost(String cost) {
		Cost = cost;
	}

	public String getFuelSurcharge() {
		return FuelSurcharge;
	}

	public void setFuelSurcharge(String fuelSurcharge) {
		FuelSurcharge = fuelSurcharge;
	}

	public String getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getField() {
		return Field;
	}

	public void setField(String field) {
		Field = field;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
