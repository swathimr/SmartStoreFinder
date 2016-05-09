package org.smartstore.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "store_template_xref")
public class StoreTemplate implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long storeTemplateId;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Store store;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Template template;
	
	public StoreTemplate(){}

	public long getStoreTemplateId() {
		return storeTemplateId;
	}

	public void setStoreTemplateId(long storeTemplateId) {
		this.storeTemplateId = storeTemplateId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
	
	
}
