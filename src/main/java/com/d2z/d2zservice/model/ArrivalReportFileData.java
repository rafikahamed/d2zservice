package com.d2z.d2zservice.model;

public class ArrivalReportFileData {
	
	private String connoteNo;
	private String status;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private String scannedDateTime;
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getConnoteNo() {
		return connoteNo;
	}
	public void setConnoteNo(String connoteNo) {
		this.connoteNo = connoteNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getScannedDateTime() {
		return scannedDateTime;
	}
	public void setScannedDateTime(String scannedDateTime) {
		this.scannedDateTime = scannedDateTime;
	}
	

}
