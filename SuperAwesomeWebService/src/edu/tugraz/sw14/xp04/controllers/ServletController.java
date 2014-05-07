package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.UserException;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;
import edu.tugraz.sw14.xp04.stubs.Response;

public class ServletController {

	private UserDAO userDAO;

	public ServletController() {
		userDAO = new UserDAO();
	}

	public ServletController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public LoginResponse login(LoginRequest request) {
		if (request == null) {
			throw new IllegalStateException("request must not be null");
		}

		LoginResponse response = new LoginResponse();

		String email = request.getId();
		String password = request.getPassword();

		if (!userDAO.userExistsByEmail(email)) {
			response.setError(true);
			response.setErrorMessage(ErrorMessages.USER_DOES_NOT_EXISTS);

		} else {
			if (password.compareTo(userDAO.getUserByEmail(email).getPassword()) == 0) {
				response.setError(false);
				response.setId(request.getId());
				
				userDAO.updateGcmId(email, request.getGcmId());
			} else {
				response.setError(true);
				response.setErrorMessage(ErrorMessages.PASSWORD_IS_WRONG);
			}
		}
		return response;
	}

	public RegistrationResponse register(RegistrationRequest request) {
		
		if(request == null) {
			throw new IllegalStateException("request must not be null");
		}
		
		RegistrationResponse response = new RegistrationResponse();
		
		try {
			checkInput(request, response);
		}
		catch(UserException e) {
			response.setError(true);
			response.setErrorMessage(e.getMessage());
			return response;
		}
		
		String email 	= request.getId();
		String name  	= request.getName();
		String password = request.getPassword();
		
		userDAO.insertUser(name, email, password);
		
		response.setError(false);
		
		return response;
	}
	
	private void checkInput(RegistrationRequest req, Response res) throws UserException
	{
		if (req.getPassword() == null || req.getPassword().equals(""))
		{
			throw new UserException(ErrorMessages.PASSWORD_IS_EMPTY);
		}
		if (req.getId() == null || req.getId().equals(""))
		{
			throw new UserException(ErrorMessages.EMAIL_IS_EMPTY);
		}
		if (req.getName() == null || req.getName().equals(""))
		{
			throw new UserException(ErrorMessages.NAME_IS_EMPTY);
		}
		if (userDAO.userExistsByEmail(req.getId()))
		{
			throw new UserException(ErrorMessages.USER_ALREADY_EXISTS);
		}
	}
	
	public User findUserByEmail(String email) {
		return userDAO.getUserByEmail(email);
	}
}
