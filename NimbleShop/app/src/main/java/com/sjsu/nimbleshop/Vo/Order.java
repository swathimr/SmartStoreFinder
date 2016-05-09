package com.sjsu.nimbleshop.Vo;

import java.util.List;



public class Order implements java.io.Serializable{

	private long orderId;

	public Order(){}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}


	
}
