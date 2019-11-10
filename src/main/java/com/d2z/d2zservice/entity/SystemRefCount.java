package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="SystemRefCount", schema="dbo")
@NamedQuery(name="SystemRefCount.findAll", query="SELECT c FROM SystemRefCount c")
public class SystemRefCount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="Prefix")
	private String prefix;

	@Column(name="SystemRefNo")
	private Integer systemRefNo;
	
	@Id
	@Column(name="Supplier")
	private String supplier;
	
	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getSystemRefNo() {
		return systemRefNo;
	}

	public void setSystemRefNo(Integer systemRefNo) {
		this.systemRefNo = systemRefNo;
	}


}