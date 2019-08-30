package com.d2z.d2zservice.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class IncomingJobResponse {

	
	private String Jobid;
	
	private String MLID;
	
	
	private String Broker;
	
	
	private String Consignee;
	

	
	private String Mawb;
	
	
	private String Destination;
	
	
	private String Flight;
	
	
	private String Piece;
	
	
	private String Hawb;
	
	
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


	


	public String getJobid() {
		return Jobid;
	}


	public void setJobid(String jobid) {
		Jobid = jobid;
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


	private String Weight;
	
	
	private String Eta;
	
	

	
	private String Ata;
	

	
	
	public String getEta() {
		return Eta;
	}


	public void setEta(String eta) {
		Eta = eta;
	}


	public String getAta() {
		return Ata;
	}


	public void setAta(String ata) {
		Ata = ata;
	}


	private String Clear;
	
	
	private String Note;
	
	
	private String Held;
	
	
	private String outturn;
	

}
