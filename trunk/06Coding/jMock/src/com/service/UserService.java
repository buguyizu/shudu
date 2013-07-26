package com.service;

import com.dao.UserDAO;
import com.model.User;

public interface UserService {
	public void setUserDAO(UserDAO userDAO);
	public void saveUser(User user);
	public User getUser(long id);

}
