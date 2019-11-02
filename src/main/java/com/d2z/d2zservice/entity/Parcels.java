package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Parcels")
public class Parcels implements Serializable{
	
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Rowid")
	private String ID;
	
	
	

	@Column(name = "MAWB")
	private String Mawb;
	

	
	@Column(name = "HAWB")
	private String Hawb;
	
	
	
	@Column(name = "NOTE")
	private String Note;
	
	@Column(name = "STATUS")
	private String Status;
	
	@Column(name = "OUTPUT")
	private String Output;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMawb() {
		return Mawb;
	}

	public void setMawb(String mawb) {
		Mawb = mawb;
	}

	public String getHawb() {
		return Hawb;
	}

	public void setHawb(String hawb) {
		Hawb = hawb;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getOutput() {
		return Output;
	}

	public void setOutput(String output) {
		Output = output;
	}
	

}
