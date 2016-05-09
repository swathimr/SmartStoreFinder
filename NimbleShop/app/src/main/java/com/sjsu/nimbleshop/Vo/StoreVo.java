package com.sjsu.nimbleshop.Vo;

import java.util.List;





public class StoreVo implements java.io.Serializable{

	
	private long storeId;
	private String storeName;
	private String storeDesc;
	private Address address;
	private List<ProductVo> listProducts;
	private List<IotDevice> listIotDevices;
	private Order order;
	
	public StoreVo(){}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreDesc() {
		return storeDesc;
	}

	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<ProductVo> getListProducts() {
		return listProducts;
	}

	public void setListProducts(List<ProductVo> listProducts) {
		this.listProducts = listProducts;
	}

	public List<IotDevice> getListIotDevices() {
		return listIotDevices;
	}

	public void setListIotDevices(List<IotDevice> listIotDevices) {
		this.listIotDevices = listIotDevices;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	

}