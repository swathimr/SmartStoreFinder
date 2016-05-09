package com.sjsu.nimbleshop.Vo;

import com.google.gson.Gson;

import java.util.List;

public class TemplateVo implements java.io.Serializable {

//	public static TemplateVo fromJson(String s) {
//		return new Gson().fromJson(s, TemplateVo.class);
//	}
//	public String toString() {
//		return new Gson().toJson(this);
//	}

	private int templateId;
	private String name;
	private List<StoreVo> stores;

	public TemplateVo() {
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
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
