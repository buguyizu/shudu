package com.dao;

import com.model.User;

public interface UserDAO {
	public void saveUser(User user);
	public User getUser(long id);
}
