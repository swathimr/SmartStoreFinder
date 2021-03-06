package org.smartstore.controller;

import org.smartstore.dao.UserDao;
import org.smartstore.model.User;
import org.smartstore.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="/user/create", method=RequestMethod.POST)
	public UserVo create(
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="name") String name,
			@RequestParam(value="password", required=true) String password) {
		
		User user = null;
		UserVo userVo = null;
		try {
			user = new User(email, name, password);
			userDao.save(user);
			userVo = new UserVo(email, name, null);
		} catch (Exception ex) {
			return null;
		}
		return userVo;
	}

}
