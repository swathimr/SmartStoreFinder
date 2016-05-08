package com.sjsu.nimbleshop.Vo;




public class IotDevice implements java.io.Serializable{

	private long iotDeviceId;
	private Long counterId;
	
	private Long countOfPeople;
	
	private Long time;

	public IotDevice(){}
	

	
	public long getIotDeviceId() {
		return iotDeviceId;
	}

	public void setIotDeviceId(long iotDeviceId) {
		this.iotDeviceId = iotDeviceId;
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
