package com.service;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.dao.UserDAO;
import com.model.User;
import com.service.impl.UserServiceImpl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class UserServiceTest extends MockObjectTestCase {
	
	private UserService userService = new UserServiceImpl();

	private Mock userDAO = null;

	public UserServiceTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		userDAO = new Mock(UserDAO.class);
		userService.setUserDAO((UserDAO) userDAO.proxy());
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(UserServiceTest.class);
		return suite;
	}

	public void testGetUser() {
		User fakeUser = new User("John");
		userDAO.expects(once()).method("getUser").with(eq(1L)).will(
				returnValue(fakeUser));
		User user = userService.getUser(1L);
		assertNotNull(user);
		assertEquals("John", user.getName());
	}

	public void testSaveUser() {
		User fakeUser = new User("John");
		
		userDAO.expects(once()).method("getUser").with(eq(1L)).will(
				returnValue(fakeUser));
		User user = userService.getUser(1L);
		
		assertEquals("John", user.getName());
		

		user.setName("Mike");
		
		userDAO.expects(once()).method("saveUser").with(same(fakeUser));
		userService.saveUser(user);
		
		userDAO.expects(once()).method("getUser").with(eq(1L)).will(
				returnValue(user));
		User modifiedUser = userService.getUser(1L);
		
		assertEquals("Mike", modifiedUser.getName());
	}
}