package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="IncomingJobs")
public class IncomingJobs implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(String isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID")
	private String ID;
	
	@Column(name = "MLID")
	private String MLID;
	
	@Column(name = "Broker")
	private String Broker;
	
	@Column(name = "Consignee")
	private String Consignee;
	

	@Column(name = "MAWB")
	private String Mawb;
	
	@Column(name = "DESTINATION")
	private String Destination;
	
	@Column(name = "FLIGHT")
	private String Flight;
	
	@Column(name = "PIECE")
	private String Piece;
	
	@Column(name = "HAWB")
	private String Hawb;
	
	@Column(name = "WEIGHT")
	private String Weight;
	
	@Column(name = "ETA")
	private LocalDate Eta;
	
	@Column(name = "ISDeleted")
	private String isDeleted;
	

	@Column(name = "ISSubmitted")
	private String isSubmitted;
	
	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMLID() {
		return MLID;
	}

	public void setMLID(String mLID) {
		MLID = mLID;
	}

	public String getBroker() {
		return Broker;
	}

	public void setBroker(String broker) {
		Broker = broker;
	}

	public String getConsignee() {
		return Consignee;
	}

	public void setConsignee(String consignee) {
		Consignee = consignee;
	}

	public String getMawb() {
		return Mawb;
	}

	public void setMawb(String mawb) {
		Mawb = mawb;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

	public String getFlight() {
		return Flight;
	}

	public void setFlight(String flight) {
		Flight = flight;
	}

	public String getPiece() {
		return Piece;
	}

	public void setPiece(String piece) {
		Piece = piece;
	}

	public String getHawb() {
		return Hawb;
	}

	public void setHawb(String hawb) {
		Hawb = hawb;
	}

	public String getWeight() {
		return Weight;
	}

	public void setWeight(String weight) {
		Weight = weight;
	}


	public LocalDate getEta() {
		return Eta;
	}

	public void setEta(LocalDate eta) {
		Eta = eta;
	}

	public LocalDate getAta() {
		return Ata;
	}

	public void setAta(LocalDate date1) {
		Ata = date1;
	}

	public String getClear() {
		return Clear;
	}

	public void setClear(String clear) {
		Clear = clear;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getHeld() {
		return Held;
	}

	public void setHeld(String held) {
		Held = held;
	}

	public String getOutturn() {
		return outturn;
	}

	public void setOutturn(String outturn) {
		this.outturn = outturn;
	}

	@Column(name = "ATA")
	private LocalDate Ata;
	

	
	@Column(name = "CLEAR")
	private String Clear;
	
	@Column(name = "NOTE")
	private String Note;
	
	@Column(name = "HELD")
	private String Held;
	
	@Column(name = "OUTTURN")
	private String outturn;
	

}
