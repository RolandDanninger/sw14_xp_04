package edu.tugraz.sw14.xp04.controllers;

import javax.persistence.EntityManager;

import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;
import edu.tugraz.sw14.xp04.stubs.Request;
import edu.tugraz.sw14.xp04.stubs.Response;

public class ServletController {

	private UserDAO userDAO;
	
	public ServletController() {
		userDAO = new UserDAO();
	}
	
	public ServletController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public RegistrationResponse login(RegistrationRequest request) {
		return null;
	}
	
	public RegistrationResponse register(RegistrationRequest request) {
		
		if(request == null) {
			throw new IllegalStateException("request must not be null");
		}
		
		RegistrationResponse response = new RegistrationResponse();
		
		if(!checkInput(request, response)) {
			return response;
		}
		
		String email 	= request.getId();
		String name  	= request.getName();
		String password = request.getPassword();
		
		userDAO.insertUser(name, email, password);
		
		response.setError(false);
		
		return response;
	}
	
	private boolean checkInput(RegistrationRequest req, Response res)
	{
		if (req.getPassword() == null || req.getPassword().equals(""))
		{
			res.setError(true);
			res.setErrorMessage(ErrorMessages.PASSWORD_IS_EMPTY);
			return false;
			
		}
		if (req.getId() == null || req.getId().equals(""))
		{
			res.setError(true);
			res.setErrorMessage(ErrorMessages.EMAIL_IS_EMPTY);
			return false;
		}
		if (req.getName() == null || req.getName().equals(""))
		{
			res.setError(true);
			res.setErrorMessage(ErrorMessages.NAME_IS_EMPTY);
			return false;
		}
		if (userDAO.userExistsByEmail(req.getId()))
		{
	        res.setError(true);
	        res.setErrorMessage(ErrorMessages.USER_ALREADY_EXISTS);
	        return false;
		}
		return true;	
	}
	
	private User findUserByEmail(String email, EntityManager em) {
		
		
		return null;
	}
}
