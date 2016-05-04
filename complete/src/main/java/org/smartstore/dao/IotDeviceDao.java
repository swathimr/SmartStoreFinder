package org.smartstore.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.smartstore.model.IotDevice;
import org.smartstore.model.Store;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface IotDeviceDao extends CrudRepository<IotDevice, Long>{
	
	public List<IotDevice> findByStore(Store store);
	
}
