package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.Table;

@Entity
@Table(name="Returns")
@NamedStoredProcedureQueries({
	   @NamedStoredProcedureQuery(name = "updatereturnsClientIdDetails", 
		  procedureName = "updatereturnsclientsid"
		 )
})
public class Returns implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ReturnId")
	private int returnId;
	
	@Column(name = "ScanType")
	private String scanType;
	
	@Column(name = "ArticleId")
	private String articleId;
	
	@Column(name = "BarcodelabelNumber")
	private String barcodelabelNumber;
	
	@Column(name = "ReferenceNumber")
	private String referenceNumber;
	
	@Column(name = "ReturnReason")
	private String returnReason;
	
	@Column(name = "BrokerName")
	private String brokerName;
	
	@Column(name = "ClientName")
	private String clientName;
	
	@Column(name = "ConsigneeName")
	private String consigneeName;
	
	@Column(name = "Carrier")
	private String carrier;
	
	@Column(name="User_Id")
	private int userId;

	@Column(name = "Client_Broker_id")
	private int clientBrokerId;
	
	@Column(name="Rate")
	private Double rate;
	
	@Column(name = "ReturnsCreatedDate")
	private String returnsCreatedDate;
	
	@Column(name = "Action")
	private String action;
	
	@Column(name = "ResendRefNumber")
	private String resendRefNumber;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "airwaybill")
	private String airwaybill;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getReturnId() {
		return returnId;
	}

	public void setReturnId(int returnId) {
		this.returnId = returnId;
	}

	public String getScanType() {
		return scanType;
	}

	public void setScanType(String scanType) {
		this.scanType = scanType;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getBarcodelabelNumber() {
		return barcodelabelNumber;
	}

	public void setBarcodelabelNumber(String barcodelabelNumber) {
		this.barcodelabelNumber = barcodelabelNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getClientBrokerId() {
		return clientBrokerId;
	}

	public void setClientBrokerId(int clientBrokerId) {
		this.clientBrokerId = clientBrokerId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getReturnsCreatedDate() {
		return returnsCreatedDate;
	}

	public void setReturnsCreatedDate(String returnsCreatedDate) {
		this.returnsCreatedDate = returnsCreatedDate;
	}

	public String getResendRefNumber() {
		return resendRefNumber;
	}

	public void setResendRefNumber(String resendRefNumber) {
		this.resendRefNumber = resendRefNumber;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAirwaybill() {
		return airwaybill;
	}

	public void setAirwaybill(String airwaybill) {
		this.airwaybill = airwaybill;
	}
	
}
