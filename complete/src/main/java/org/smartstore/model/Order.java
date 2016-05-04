package org.smartstore.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long orderId;

	@ManyToOne(optional=false)
	@JoinColumn(name="userId")
	private User user;
	
	@OneToMany(mappedBy="order", targetEntity=Product.class, fetch=FetchType.EAGER)
	private List<Store> listStores;
	
	@OneToMany(mappedBy="order", targetEntity=Product.class, fetch=FetchType.EAGER)
	private List<Product> listProducts;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Store> getListStores() {
		return listStores;
	}

	public void setListStores(List<Store> listStores) {
		this.listStores = listStores;
	}

	public List<Product> getListProducts() {
		return listProducts;
	}

	public void setListProducts(List<Product> listProducts) {
		this.listProducts = listProducts;
	}
	
	
}
