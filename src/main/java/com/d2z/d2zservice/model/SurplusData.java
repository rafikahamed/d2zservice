package com.d2z.d2zservice.model;

public class SurplusData {

	private String articleId;
	private String status;
	private String note;
	
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public SurplusData(Object[] row) {
		 articleId = (String)row[0];
		 status = (String)row[1];
		 note = (String)row[2];
	}
	
}
