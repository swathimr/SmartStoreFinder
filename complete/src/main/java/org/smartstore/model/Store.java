package org.smartstore.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "stores")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="storeId")
public class Store implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long storeId;

	@NotNull
	private String storeName;
	
	private String storeDesc;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Address address;
	
	
	@OneToMany(mappedBy="store", targetEntity=Product.class, fetch=FetchType.EAGER)
	private List<Product> listProducts;
	
	@OneToMany(mappedBy="store", targetEntity=IotDevice.class, fetch=FetchType.EAGER)
	private List<IotDevice> listIotDevices;
	
	@ManyToOne
	@JoinColumn(name="orderId")
	private Order order;
	
	public Store(){}
	
	public Store( String storeName, String storeDesc ){
		this.storeName = storeName;
		this.storeDesc = storeDesc;
	}


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


	public List<Product> getListProducts() {
		return listProducts;
	}


	public void setListProducts(List<Product> listProducts) {
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
