package com.d2z.d2zservice.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the ConsignmentCount database table.
 * 
 */
@Entity
@Table(name="ConsignmentCount", schema="dbo")
@NamedQuery(name="ConsignmentCount.findAll", query="SELECT c FROM ConsignmentCount c")
public class ConsignmentCount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="LabelFinish")
	private long labelFinish;

	@Column(name="LabelStart")
	private long labelStart;

	@Column(name="LabelTostart")
	private long labelTostart;
	
	@Id
	@Column(name="MLID")
	private String mlid;
	
	@Column(name="Supplier")
	private String supplier;

	@OneToMany(mappedBy = "consignmentCount")
    private List<SenderdataMaster> senderData;
	
	
	
	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public List<SenderdataMaster> getSenderData() {
		return senderData;
	}

	public void setSenderData(List<SenderdataMaster> senderData) {
		this.senderData = senderData;
	}

	public ConsignmentCount() {
	}

	public long getLabelFinish() {
		return this.labelFinish;
	}

	public void setLabelFinish(long labelFinish) {
		this.labelFinish = labelFinish;
	}

	public long getLabelStart() {
		return this.labelStart;
	}

	public void setLabelStart(long labelStart) {
		this.labelStart = labelStart;
	}

	public long getLabelTostart() {
		return this.labelTostart;
	}

	public void setLabelTostart(long labelTostart) {
		this.labelTostart = labelTostart;
	}

	public String getMlid() {
		return this.mlid;
	}

	public void setMlid(String mlid) {
		this.mlid = mlid;
	}

}