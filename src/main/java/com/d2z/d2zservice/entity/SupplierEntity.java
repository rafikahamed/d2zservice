package com.d2z.d2zservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Entity
@Table(name="Supplier")
@NamedQuery(name="SupplierEntity.findAll", query="SELECT t FROM SupplierEntity t")
public class SupplierEntity {

	@Id
	@Column(name="Supplier_Auth_Id")
	private int supplierAuthId;
	
	@Column(name="Supplier_Name")
	private String supplierName;
	
	@Column(name="Supplier_Key")
	private String supplierKey;
	
	@Column(name="Supplier_Token")
	private String supplierToken;
	
	@Column(name="Create_URI")
	private String supplierCreateUri;
	
	@Column(name="Allocate_URI")
	private String supplierAllocateUri;
	
	@Column(name="Tracking_URI")
	private String supplierTrackingUri;
	
	@Column(name="Label_URI")
	private String supplierLabelUri;
	
	
	
	public int getSupplierAuthId() {
		return supplierAuthId;
	}
	public void setSupplierAuthId(int supplierAuthId) {
		this.supplierAuthId = supplierAuthId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierKey() {
		return supplierKey;
	}
	public void setSupplierKey(String supplierKey) {
		this.supplierKey = supplierKey;
	}
	public String getSupplierToken() {
		return supplierToken;
	}
	public void setSupplierToken(String supplierToken) {
		this.supplierToken = supplierToken;
	}
	public String getSupplierCreateUri() {
		return supplierCreateUri;
	}
	public void setSupplierCreateUri(String supplierCreateUri) {
		this.supplierCreateUri = supplierCreateUri;
	}
	public String getSupplierAllocateUri() {
		return supplierAllocateUri;
	}
	public void setSupplierAllocateUri(String supplierAllocateUri) {
		this.supplierAllocateUri = supplierAllocateUri;
	}
	public String getSupplierTrackingUri() {
		return supplierTrackingUri;
	}
	public void setSupplierTrackingUri(String supplierTrackingUri) {
		this.supplierTrackingUri = supplierTrackingUri;
	}
	public String getSupplierLabelUri() {
		return supplierLabelUri;
	}
	public void setSupplierLabelUri(String supplierLabelUri) {
		this.supplierLabelUri = supplierLabelUri;
	}
	
	
}
