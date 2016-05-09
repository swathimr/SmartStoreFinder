package org.smartstore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "iotdevices")
public class IotDevice implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long iotDeviceId;

	@ManyToOne(optional=false)
	@JoinColumn(name="storeId")
	private Store store;
	
	@NotNull
	private Long counterId;
	
	private Long countOfPeople;
	
	private Long time;

	public IotDevice(){}
	
	public IotDevice( Store store ){
		this.store = store;
	}
	
	
	public long getIotDeviceId() {
		return iotDeviceId;
	}

	public void setIotDeviceId(long iotDeviceId) {
		this.iotDeviceId = iotDeviceId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Long getCounterId() {
		return counterId;
	}

	public void setCounterId(Long counterId) {
		this.counterId = counterId;
	}

	public Long getCountOfPeople() {
		return countOfPeople;
	}

	public void setCountOfPeople(Long countOfPeople) {
		this.countOfPeople = countOfPeople;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	
	
	
	
}
