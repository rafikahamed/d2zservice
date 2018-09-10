package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the UserRole database table.
 * 
 */
@Entity
@NamedQuery(name="UserRole.findAll", query="SELECT u FROM UserRole u")
public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="Role_Id")
	private int role_Id;

	@Column(name="User_Role")
	private String user_Role;

	public UserRole() {
	}

	public int getRole_Id() {
		return this.role_Id;
	}

	public void setRole_Id(int role_Id) {
		this.role_Id = role_Id;
	}

	public String getUser_Role() {
		return this.user_Role;
	}

	public void setUser_Role(String user_Role) {
		this.user_Role = user_Role;
	}

}