package edu.tugraz.sw14.xp04;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.controllers.ContactController;
import edu.tugraz.sw14.xp04.controllers.LoginController;
import edu.tugraz.sw14.xp04.controllers.RegistrationController;
import edu.tugraz.sw14.xp04.controllers.SendMessageController;
import edu.tugraz.sw14.xp04.gcm.GCMConnection;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
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
			} else if (action.compareTo("addcontact") == 0) {
				addContact(request, response);
			}

		} catch (ServerException e) {
			System.err.println("Server Exception: " + e.getMessage());
			throw new ServletException(e.getMessage());
		}

	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, IOException {
		LoginRequest req = null;

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					LoginRequest.class);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginRequest.", e);
		}

		LoginController controller = new LoginController();
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
			HttpServletResponse response) throws ServerException, IOException {
		RegistrationRequest req = null;

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					RegistrationRequest.class);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginRequest.", e);
		}

		RegistrationController controller = new RegistrationController();
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
		SendMessageRequest req = null;

		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new ServerException("invalid session");
		}

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					SendMessageRequest.class);
		} catch (Exception e) {
			System.err.println("Failed to parse SendMessageRequest.");
			throw new ServerException("Failed to parse SendMessageRequest.", e);
		}

		GCMConnection gcmConnection = new GCMConnection();
		SendMessageController controller = new SendMessageController(
				gcmConnection);
		String sender = (String) session.getAttribute("id");
		SendMessageResponse res = controller.sendMessage(req, sender);

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException(
					"Failed to parse RegistrationResponse to JSON.", e);
		}
	}

	private void addContact(HttpServletRequest request,
			HttpServletResponse response) throws ServerException {
		AddContactRequest req = null;

		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new ServerException("invalid session");
		}

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					AddContactRequest.class);
		} catch (Exception e) {
			System.err.println("Failed to parse AddContactRequest.");
			throw new ServerException("Failed to parse AddContactRequest.", e);
		}

		ContactController controller = new ContactController();
		AddContactResponse res = controller.getContactInfo(req);

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException(
					"Failed to parse AddContactResponse to JSON.", e);
		}
	}
}
