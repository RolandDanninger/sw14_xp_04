package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;

public abstract class ServletController {

	protected UserDAO userDAO;

	public ServletController() {
		userDAO = new UserDAO();
	}

	public ServletController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public User findUserByEmail(String email) {
		return userDAO.getUserByEmail(email);
	}

}