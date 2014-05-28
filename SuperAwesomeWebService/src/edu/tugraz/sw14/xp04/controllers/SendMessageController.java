package edu.tugraz.sw14.xp04.controllers;

import edu.tugraz.sw14.xp04.UserException;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.gcm.GCMConnection;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class SendMessageController extends ServletController {

	private GCMConnection gcmCon;

	public SendMessageController(GCMConnection gcmCon) {
		super();
		this.gcmCon = gcmCon;
	}

	public SendMessageController(UserDAO userDAO) {
		super(userDAO);
	}

	public SendMessageController(GCMConnection gcmCon, UserDAO userDAO) {
		super(userDAO);
		this.gcmCon = gcmCon;
	}

	public SendMessageResponse sendMessage(SendMessageRequest request,
			String sender) {

		if (request == null) {
			throw new IllegalStateException("request must not be null");
		}

		SendMessageResponse response = new SendMessageResponse();

		try {
			checkInput(request);
		} catch (UserException e) {
			response.setError(true);
			response.setErrorMessage(e.getMessage());
			return response;
		}

		User receiver = findUserByEmail(request.getReceiverId());

		long timestamp = System.currentTimeMillis();
		
		boolean bool = gcmCon.sendMessage(request, sender, receiver.getGcmId(), timestamp);

		if (bool) {
			response.setError(false);
			response.setErrorMessage(null);
			response.setId(receiver.getEmail());
			response.setContent(request.getMessage());
			response.setTimestamp(timestamp);
		} else {
			response.setError(true);
			response.setErrorMessage("gcm connection failed");
		}

		return response;
	}

	private void checkInput(SendMessageRequest req) throws UserException {

		if (req.getReceiverId() == null || req.getReceiverId().equals("")) {
			throw new UserException(ErrorMessages.RECEIVER_IS_EMPTY);
		}
		if (req.getMessage() == null || req.getMessage().equals("")) {
			throw new UserException(ErrorMessages.MESSAGE_IS_EMPTY);
		}
		if (!userDAO.existsByEmail(req.getReceiverId())) {
			throw new UserException(ErrorMessages.RECEIVER_DOES_NOT_EXIST);
		}

	}
}
