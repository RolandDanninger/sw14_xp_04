package edu.tugraz.sw14.xp04;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.manager.EMFService;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

public class SuperAwesomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper jsonMapper = null;

	public SuperAwesomeServlet() {
		this.jsonMapper = new ObjectMapper();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = (String) request.getParameter("action");

		if (action == null) {
			return;
		}

		response.getWriter().println("Action = " + action);

		EntityManager em = EMFService.get().createEntityManager();

		User newUser = new User();
		newUser.setName(action);

		try {

			if (User.findSingleUser(User.NAMED_QUERY_NAME,
					User.QUERY_PARAM_NAME, action, em) == null) {
				em.persist(newUser);
			} else {
				response.getWriter().println("username already exists");
			}
		} catch (Exception e) {
			response.getWriter().println(e.getLocalizedMessage());
		} finally {
			em.close();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = (String) request.getParameter("action");

		if (action == null) {
			return;
		}
		if (action.compareTo("login") == 0) {
			login(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) {
		try {
			LoginRequest req = jsonMapper.readValue(request.getInputStream(),
					LoginRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		LoginResponse res = new LoginResponse();
		res.setError(true);

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
