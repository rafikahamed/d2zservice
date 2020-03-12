package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
	
	@Column(name="Process")
	private BigDecimal process;
	
	@Column(name="Pickup")
	private BigDecimal pickup;
	
	@Column(name="Docs")
	private BigDecimal docs;
	
	@Column(name="Airport")
	private BigDecimal airport;
	
	@Column(name="Total")
	private BigDecimal total;
	
	@Column(name = "InjectionDate")
	private LocalDate injectionDate;
	
	@Column(name = "ClearanceDate")
	private LocalDate clearanceDate;
	
	@Column(name = "SurplusShortage")
	private String surplusShortage;
	
	@Column(name = "DamageNotes")
	private String damageNotes;
	
	
	public LocalDate getInjectionDate() {
		return injectionDate;
	}

	public void setInjectionDate(LocalDate injectionDate) {
		this.injectionDate = injectionDate;
	}

	public LocalDate getClearanceDate() {
		return clearanceDate;
	}

	public void setClearanceDate(LocalDate clearanceDate) {
		this.clearanceDate = clearanceDate;
	}

	public String getSurplusShortage() {
		return surplusShortage;
	}

	public void setSurplusShortage(String surplusShortage) {
		this.surplusShortage = surplusShortage;
	}

	public String getDamageNotes() {
		return damageNotes;
	}

	public void setDamageNotes(String damageNotes) {
		this.damageNotes = damageNotes;
	}

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

	public BigDecimal getProcess() {
		return process;
	}

	public void setProcess(BigDecimal process) {
		this.process = process;
	}

	public BigDecimal getPickup() {
		return pickup;
	}

	public void setPickup(BigDecimal pickup) {
		this.pickup = pickup;
	}

	public BigDecimal getDocs() {
		return docs;
	}

	public void setDocs(BigDecimal docs) {
		this.docs = docs;
	}

	public BigDecimal getAirport() {
		return airport;
	}

	public void setAirport(BigDecimal airport) {
		this.airport = airport;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
