package com.service.impl;

import com.dao.UserDAO;
import com.model.User;
import com.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDAO userDAO;
	
	public User getUser(long id) {
		return userDAO.getUser(id);
	}

	public void saveUser(User user) {
		userDAO.saveUser(user);
		
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
		
	}

}
