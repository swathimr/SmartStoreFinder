package org.smartstore.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "template")
public class Template implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long templateId;

	// The category of the item
	@NotNull
	private String category;

	// The item name
	@NotNull
	private String name;
	
	
	public Template(){}
	
	public Template(String category, String name) {
		this.category = category;
		this.name = name;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
