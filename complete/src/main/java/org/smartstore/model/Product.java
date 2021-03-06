package org.smartstore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "products")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="productId")
public class Product implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long productId;

	@ManyToOne(optional=false)
	@JoinColumn(name="storeId")
	private Store store;
	
	@NotNull
	private String productName;
	
	@NotNull
	private String productDesc;

	@NotNull
	private Double cost;
	
	@NotNull
	private Long quantity;
	
	@ManyToOne
	@JoinColumn(name="orderId")
	private Order order;
	
	public Product(){}
	
	public Product( String productName, String productDesc ){
		this.productName = productName;
		this.productDesc = productDesc;
		
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	

}
