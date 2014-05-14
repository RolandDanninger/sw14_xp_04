package edu.tugraz.sw14.xp04;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.controllers.ServletController;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.stubs.GCMMessage;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class SuperAwesomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper jsonMapper = null;

	public SuperAwesomeServlet() {
		this.jsonMapper = new ObjectMapper();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			return;
		}

		try {
			if (action.compareTo("login") == 0) {
				login(request, response);
			} else if (action.compareTo("register") == 0) {
				register(request, response);
			} else if (action.compareTo("send") == 0) {
				sendMessage(request, response);
			}
		} catch (ServerException e) {

		} catch (UserException e) {

		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, IOException, UserException {
		LoginRequest req = null;

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					LoginRequest.class);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginRequest.", e);
		}

		ServletController controller = new ServletController();
		LoginResponse res = controller.login(req);

		if (!res.isError()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("id", req.getId());
			session.setMaxInactiveInterval(60 * 60); // time in s(1h)
		}

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginResponse to JSON.",
					e);
		}

	}

	private void register(HttpServletRequest request,
			HttpServletResponse response) throws ServerException, IOException,
			UserException {
		RegistrationRequest req = null;

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					RegistrationRequest.class);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginRequest.", e);
		}

		ServletController controller = new ServletController();
		RegistrationResponse res = controller.register(req);

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException(
					"Failed to parse RegistrationResponse to JSON.", e);
		}
	}

	private void sendMessage(HttpServletRequest request,
			HttpServletResponse response) throws ServerException, IOException {
		ServletController controller = new ServletController();
		SendMessageRequest req = null;

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					SendMessageRequest.class);
		} catch (Exception e) {
			System.err.println("Failed to parse SendMessageRequest.");
			throw new ServerException("Failed to parse SendMessageRequest.", e);
		}

		URL url = null;

		try {
			url = new URL("https://android.googleapis.com/gcm/send");
		} catch (MalformedURLException e) {
			System.err.println("invalid url sendMessage");
			throw new ServerException("invalid url sendMessage");
		}

		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new ServerException("invalid session");
		}

		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization",
					"key=AIzaSyDaEIpAaziLsL9SVA5N3_MzPdf8FLVA7wA");
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			User receiver = controller.findUserByEmail(req.getReceiverId());

			GCMMessage msg = new GCMMessage();
			msg.setRegistration_ids(Arrays.asList(receiver.getGcmId()));

			System.err.println(receiver.getGcmId());

			String id = (String) session.getAttribute("id");
			Map<String, String> data = new HashMap<String, String>();
			data.put("message", req.getMessage());
			data.put("sender", id);
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

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException(
					"Failed to parse RegistrationResponse to JSON.", e);
		}
	}
}
