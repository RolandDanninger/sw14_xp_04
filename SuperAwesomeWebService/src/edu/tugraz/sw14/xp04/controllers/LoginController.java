package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.UserException;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;

public class LoginController extends ServletController {

	public LoginController() {
		super();
	}

	public LoginController(UserDAO userDAO) {
		super(userDAO);
	}

	public LoginResponse login(LoginRequest request) {
		if (request == null) {
			throw new IllegalStateException("request must not be null");
		}

		LoginResponse response = new LoginResponse();

		try {
			checkInput(request);
		} catch (UserException e) {
			response.setError(true);
			response.setErrorMessage(e.getMessage());
			return response;
		}

		String email = request.getId();
		String password = request.getPassword();

		if (password.compareTo(userDAO.getByEmail(email).getPassword()) == 0) {
			response.setError(false);
			response.setId(request.getId());

			userDAO.updateGcmId(email, request.getGcmId());
		} else {
			response.setError(true);
			response.setErrorMessage(ErrorMessages.PASSWORD_IS_WRONG);
		}

		return response;
	}

	private void checkInput(LoginRequest req) throws UserException {
		if (req.getPassword() == null || req.getPassword().equals("")) {
			throw new UserException(ErrorMessages.PASSWORD_IS_EMPTY);
		}
		if (req.getId() == null || req.getId().equals("")) {
			throw new UserException(ErrorMessages.EMAIL_IS_EMPTY);
		}
		if (req.getGcmId() == null || req.getGcmId().equals("")) {
			throw new UserException(ErrorMessages.GCM_ID_IS_EMPTY);
		}
		if (!userDAO.existsByEmail(req.getId())) {
			throw new UserException(ErrorMessages.USER_DOES_NOT_EXISTS);
		}
	}
}
