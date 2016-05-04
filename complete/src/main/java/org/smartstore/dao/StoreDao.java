package org.smartstore.dao;

import javax.transaction.Transactional;

import org.smartstore.model.Store;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface StoreDao extends CrudRepository<Store, Long>{
	
}
