package org.smartstore.vo;

import java.util.List;

import org.smartstore.model.Address;
import org.smartstore.model.IotDevice;
import org.smartstore.model.Order;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="storeId")
public class StoreVo implements java.io.Serializable{

	
	private long storeId;
	private String storeName;
	private String storeDesc;
	private Address address;
	private List<ProductVo> listProducts;
	private List<IotDevice> listIotDevices;
	private Order order;
	private Long travelTime;
	
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

	public Long getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Long travelTime) {
		this.travelTime = travelTime;
	}
	
	

}
