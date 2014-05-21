package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.UserException;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class RegistrationController extends ServletController {

	public RegistrationController() {
		super();
	}

	public RegistrationController(UserDAO userDAO) {
		super(userDAO);
	}

	public RegistrationResponse register(RegistrationRequest request) {

		if (request == null) {
			throw new IllegalStateException("request must not be null");
		}

		RegistrationResponse response = new RegistrationResponse();

		try {
			checkInput(request);
		} catch (UserException e) {
			response.setError(true);
			response.setErrorMessage(e.getMessage());
			return response;
		}

		String email = request.getId();
		String name = request.getName();
		String password = request.getPassword();

		User u = new User(email, name, password);

		userDAO.insert(u);

		response.setError(false);

		return response;
	}

	private void checkInput(RegistrationRequest req) throws UserException {
		if (req.getPassword() == null || req.getPassword().equals("")) {
			throw new UserException(ErrorMessages.PASSWORD_IS_EMPTY);
		}
		if (req.getId() == null || req.getId().equals("")) {
			throw new UserException(ErrorMessages.EMAIL_IS_EMPTY);
		}
		if (req.getName() == null || req.getName().equals("")) {
			throw new UserException(ErrorMessages.NAME_IS_EMPTY);
		}
		if (userDAO.existsByEmail(req.getId())) {
			throw new UserException(ErrorMessages.USER_ALREADY_EXISTS);
		}
	}
}
