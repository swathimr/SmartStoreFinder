package org.smartstore.dao;

import javax.transaction.Transactional;

import org.smartstore.model.User;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface UserDao extends CrudRepository<User, Long>{
	
	public User findByEmail(String email);
	
}
