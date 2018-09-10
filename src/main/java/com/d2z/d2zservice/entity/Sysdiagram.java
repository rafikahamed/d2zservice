package com.d2z.d2zservice.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sysdiagrams database table.
 * 
 */
@Entity
@Table(name="sysdiagrams")
@NamedQuery(name="Sysdiagram.findAll", query="SELECT s FROM Sysdiagram s")
public class Sysdiagram implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="diagram_id")
	private int diagramId;

	private String definition;

	private String name;

	@Column(name="principal_id")
	private int principalId;

	private int version;

	public Sysdiagram() {
	}

	public int getDiagramId() {
		return this.diagramId;
	}

	public void setDiagramId(int diagramId) {
		this.diagramId = diagramId;
	}

	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(int principalId) {
		this.principalId = principalId;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}