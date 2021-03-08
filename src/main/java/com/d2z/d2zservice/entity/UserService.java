package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="UserService.findAll", query="SELECT u FROM UserService u")
public class UserService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="RowId")
	private int rowId;

	@Column(name="User_Id")
	private int userId;
	
	@Column(name="InjectionType")
	private String injectionType;
	
	@Column(name="CompanyName")
	private String companyName;

	@Column(name="User_Name")
	private String user_Name;
	
	@Column(name="ServiceType")
	private String serviceType;
	
	@Column(name="Service_IsDeleted")
	private boolean service_isDeleted;
	
	@Column(name="Autoshipment")
	private String autoShipment;
	
	@Column(name="Timestamp")
	private Timestamp timestamp;

	@Column(name="ModifiedTimestamp")
	private Timestamp modifiedTimestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id",insertable=false, updatable=false)
    private User users;

    public User getUsers() {
		return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }
	public Timestamp getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	
	
	
	public String getAutoShipment() {
		return autoShipment;
	}

	public void setAutoShipment(String autoShipment) {
		this.autoShipment = autoShipment;
	}

	public String getInjectionType() {
		return this.injectionType;
	}

	public void setInjectionType(String injectionType) {
		this.injectionType = injectionType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUser_Name() {
		return user_Name;
	}

	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public boolean isService_isDeleted() {
		return service_isDeleted;
	}

	public void setService_isDeleted(boolean service_isDeleted) {
		this.service_isDeleted = service_isDeleted;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
