package edu.tugraz.sw14.xp04.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.ServerException;
import edu.tugraz.sw14.xp04.UserException;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.GCMMessage;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class SendMessageController extends ServletController {

	private final String API_KEY = "AIzaSyDaEIpAaziLsL9SVA5N3_MzPdf8FLVA7wA";
	private ObjectMapper jsonMapper = null;

	public SendMessageController() {
		super();
		this.jsonMapper = new ObjectMapper();
	}

	public SendMessageController(UserDAO userDAO) {
		super(userDAO);
	}

	public SendMessageResponse sendMessage(SendMessageRequest request,
			String sender) throws ServerException, IOException {

		if (request == null) {
			throw new IllegalStateException("request must not be null");
		}

		SendMessageResponse response = new SendMessageResponse();

		URL url = null;

		try {
			url = new URL("https://android.googleapis.com/gcm/send");
		} catch (MalformedURLException e) {
			System.err.println("invalid url sendMessage");
			throw new ServerException("invalid url sendMessage");
		}

		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "key=" + API_KEY);
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			User receiver = findUserByEmail(request.getReceiverId());

			GCMMessage msg = new GCMMessage();
			msg.setRegistration_ids(Arrays.asList(receiver.getGcmId()));

			System.err.println(receiver.getGcmId());

			Map<String, String> data = new HashMap<String, String>();
			data.put("message", request.getMessage());
			data.put("sender", sender);
			msg.setData(data);

			OutputStream out = connection.getOutputStream();

			String jsonGCMMsg = jsonMapper.writeValueAsString(msg);

			System.err.println(jsonGCMMsg);

			out.write(jsonGCMMsg.getBytes());
			out.close();

			int status = connection.getResponseCode();
			if (status == 200) {
				InputStream is = connection.getInputStream();
				InputStreamReader isReader = new InputStreamReader(is);
				BufferedReader bReader = new BufferedReader(isReader);

				String line = "";
				while ((line = bReader.readLine()) != null) {
					System.err.println(line);
				}

				bReader.close();
			} else {
				System.err.println("Post failed with error code ");
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		SendMessageResponse res = new SendMessageResponse();
		res.setError(false);
		res.setErrorMessage(null);

		return response;
	}

	private void checkInput(SendMessageRequest req) throws UserException {

		if (req.getReceiverId() == null || req.getReceiverId().equals("")) {
			throw new UserException(ErrorMessages.RECEIVER_IS_EMPTY);
		}
		if (req.getMessage() == null || req.getMessage().equals("")) {
			throw new UserException(ErrorMessages.MESSAGE_IS_EMPTY);
		}
		if (userDAO.userExistsByEmail(req.getReceiverId())) {
			throw new UserException(ErrorMessages.RECEIVER_DOES_NOT_EXIST);
		}

	}
}
