package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

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
}
