package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.UserException;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
import edu.tugraz.sw14.xp04.stubs.ContactStub;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;

public class ContactController extends ServletController {

	public ContactController() {
		super();
	}

	public ContactController(UserDAO userDAO) {
		super(userDAO);
	}

	public AddContactResponse getContactInfo(AddContactRequest request) {
		if (request == null) {
			throw new IllegalStateException("request must not be null");
		}

		AddContactResponse response = new AddContactResponse();

		User user = findUserByEmail(request.getId());
		if (user == null) {
			response.setError(true);
			response.setErrorMessage(ErrorMessages.USER_DOES_NOT_EXISTS);
		} else {
			response.setError(false);
			response.setErrorMessage(null);

			// TODO change image url parameter
			ContactStub contactInfo = new ContactStub();
			contactInfo.setEmail(user.getEmail());
			contactInfo.setName(user.getName());
			contactInfo.setImgUrl(null);

			response.setContact(contactInfo);
		}

		return response;
	}

}
