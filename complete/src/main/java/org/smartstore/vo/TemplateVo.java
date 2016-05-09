package org.smartstore.vo;

import java.util.List;

public class TemplateVo implements java.io.Serializable {

	private long templateId;
	private String name;
	private List<StoreVo> stores;

	public TemplateVo() {
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StoreVo> getStores() {
		return stores;
	}

	public void setStores(List<StoreVo> stores) {
		this.stores = stores;
	}

}
