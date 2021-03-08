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
@Table(name = "FFResponse")
public class FFResponse implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name = "RoWID")
	//private int rowId;
	
	@Column(name = "Reference_number")
	private String referencenumber;
	
	@Column(name = "Barcodelabelnumber")
	private String barcodelabelnumber;
	
	@Column(name = "Weight")
	private String weight;
	
	@Column(name = "message_no")
	private String message;
	
	@Id
	@Column(name = "ArticleId")
	private String articleid;
	
	@Column(name = "Supplier")
	private String supplier;
	
	@Column(name = "Response")
	private String response;
	
	@Column(name = "Timestamp")
	private Timestamp timestamp;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReferencenumber() {
		return referencenumber;
	}

	public void setReferencenumber(String referencenumber) {
		this.referencenumber = referencenumber;
	}

	public String getBarcodelabelnumber() {
		return barcodelabelnumber;
	}

	public void setBarcodelabelnumber(String barcodelabelnumber) {
		this.barcodelabelnumber = barcodelabelnumber;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getArticleid() {
		return articleid;
	}

	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
