package org.smartstore.controller;

import java.util.List;

import org.smartstore.dao.IotDeviceDao;
import org.smartstore.dao.StoreDao;
import org.smartstore.model.IotDevice;
import org.smartstore.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IotDeviceController {

	@Autowired
	private IotDeviceDao iotDeviceDao;
	
	@Autowired
	private StoreDao storeDao;
	
	@RequestMapping(value = "/iot/create", method = RequestMethod.POST)
	public ResponseEntity<IotDevice> create(
			@RequestParam(value="storeId" , required=true) Long storeId
			/*@RequestParam(value="counterId") Long counterId*/) {
		
		IotDevice iotDevice = null;
		Store store = null;
		try {
			
			if( null != storeId && 0 != storeId ){
				store = storeDao.findOne(storeId);
				if( null != store ){
					iotDevice = new IotDevice( store );
					iotDeviceDao.save(iotDevice);
				}
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<IotDevice>( iotDevice, HttpStatus.CREATED );
	}
	
	
	@RequestMapping(value = "/iot/get", method = RequestMethod.GET)
	public ResponseEntity<List<IotDevice>> get(
			@RequestParam(value="storeId", required=true) Long storeId
			/*@RequestParam(value="counterId") Long counterId*/) {
		
		List<IotDevice> list = null;
		Store store = null;
		try {
			
			if( null != storeId && 0 != storeId ){
				store = storeDao.findOne(storeId);
				if( null != store ){
					list = iotDeviceDao.findByStore(store);
				}
			}
		} catch (Exception ex) {
			return new ResponseEntity<List<IotDevice>>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<IotDevice>>(list, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/iot/update", method = RequestMethod.PUT)
	public ResponseEntity<IotDevice> update(
			@RequestParam(value="iotDeviceId", required=true) Long iotDeviceId,
			@RequestParam(value="countOfPeople", required=true) Long countOfPeople,
			@RequestParam(value="time", required=true) Long time 
			) {
		
		IotDevice iotDevice = null;
		try {
			
			if( null != iotDeviceId && 0 != iotDeviceId ){
				iotDevice = iotDeviceDao.findOne(iotDeviceId);
				if( null != iotDevice ){
					
					iotDevice.setCountOfPeople(countOfPeople);
					iotDevice.setTime(time);
					iotDeviceDao.save(iotDevice);
				}
			}
		} catch (Exception ex) {
			return new ResponseEntity<IotDevice>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<IotDevice>( iotDevice, HttpStatus.OK);
	}
	
	
}
