package org.smartstore.dao;

import javax.transaction.Transactional;

import org.smartstore.model.Address;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface AddressDao extends CrudRepository<Address, Long>{
	
}
