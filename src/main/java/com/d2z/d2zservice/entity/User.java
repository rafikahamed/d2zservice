package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the Users database table.
 * 
 */
@Entity
@Table(name="Users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="Address")
	private String address;

	@Column(name="CompanyName")
	private String companyName;

	@Column(name="Country")
	private String country;

	@Column(name="EmailAddress")
	private String emailAddress;

	@Column(name="Name")
	private String name;

	@Column(name="PhoneNumber")
	private String phoneNumber;

	@Column(name="Postcode")
	private String postcode;

	@Column(name="Role_Id")
	private int role_Id;

	@Column(name="State")
	private String state;

	@Column(name="Suburb")
	private String suburb;

	@Column(name="Timestamp")
	private Timestamp timestamp;

	@Column(name="ModifiedTimestamp")
	private Timestamp modifiedTimestamp;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="User_Id")
	private int user_Id;

	@Column(name="User_IsDeleted")
	private boolean user_IsDeleted;
	
	@Column(name="client_broker_id")
	private String clientBrokerId;
	
	private String eBayToken;
	
	@Column(name="User_Name")
	private String user_Name;

	@Column(name="User_Password")
	private String user_Password;

	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<UserService> userService;

	public User() {
	}
	
	public Set<UserService> getUserService() {
		return userService;
	}

	public void setUserService(Set<UserService> userService) {
		this.userService = userService;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public int getRole_Id() {
		return this.role_Id;
	}

	public void setRole_Id(int role_Id) {
		this.role_Id = role_Id;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getUser_Id() {
		return this.user_Id;
	}

	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}

	public boolean getUser_IsDeleted() {
		return this.user_IsDeleted;
	}

	public void setUser_IsDeleted(boolean user_IsDeleted) {
		this.user_IsDeleted = user_IsDeleted;
	}

	public String getUser_Name() {
		return this.user_Name;
	}

	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}

	public String getUser_Password() {
		return this.user_Password;
	}

	public void setUser_Password(String user_Password) {
		this.user_Password = user_Password;
	}
	public Timestamp getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}
	
	public String getEBayToken() {
		return this.eBayToken;
	}

	public void setEBayToken(String eBayToken) {
		this.eBayToken = eBayToken;
	}
	
	public String getClientBrokerId() {
		return this.clientBrokerId;
	}

	public void setClientBrokerId(String clientBrokerId) {
		this.clientBrokerId = clientBrokerId;
	}

}