package com.d2z.d2zservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="eBayResponse")
@NamedQuery(name="EbayResponse.findAll", query="SELECT e FROM EbayResponse e")
public class EbayResponse implements Serializable {

		private static final long serialVersionUID = 1L; 
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="RowID")
	    private int rowId;
		
		@Column(name="Ack")
		private String ack;
		
		@Column(name="ShortMessage")
		private String shortMessage;
		
		@Column(name="LongMessage")
		private String longMessage;
		
		@Column(name="ErrorCode")
		private String errorCode;
		
		@Column(name="SeverityCode")
		private String severityCode;
		
		@Column(name="ErrorClassification")
		private String errorClassification;
		
		@Column(name="Timestamp")
		private String timestamp;

		public int getRowId() {
			return rowId;
		}

		public void setRowId(int rowId) {
			this.rowId = rowId;
		}

		public String getAck() {
			return ack;
		}

		public void setAck(String ack) {
			this.ack = ack;
		}

		public String getShortMessage() {
			return shortMessage;
		}

		public void setShortMessage(String shortMessage) {
			this.shortMessage = shortMessage;
		}

		public String getLongMessage() {
			return longMessage;
		}

		public void setLongMessage(String longMessage) {
			this.longMessage = longMessage;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getSeverityCode() {
			return severityCode;
		}

		public void setSeverityCode(String severityCode) {
			this.severityCode = severityCode;
		}

		public String getErrorClassification() {
			return errorClassification;
		}

		public void setErrorClassification(String errorClassification) {
			this.errorClassification = errorClassification;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		
		
}
