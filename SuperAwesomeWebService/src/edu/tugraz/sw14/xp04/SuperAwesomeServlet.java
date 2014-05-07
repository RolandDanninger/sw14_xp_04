package edu.tugraz.sw14.xp04;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.FoundIndex;

import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.entities.manager.EMFService;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;
import edu.tugraz.sw14.xp04.stubs.Request;
import edu.tugraz.sw14.xp04.stubs.Response;

public class SuperAwesomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper jsonMapper = null;

	public SuperAwesomeServlet() {
		this.jsonMapper = new ObjectMapper();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = (String) request.getParameter("action");

//		if (action == null) {
//			return;
//		}
//	response.getWriter().println("Action = " + action);
//	
//	UserDAO user_dao = new UserDAO();
//
//
		try {
			login(request, response);
		} catch (ServerException | UserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		
//		User newUser = new User();
//		newUser.setName(action);
//
//		try {
//
//			if (User.findSingleUser(User.NAMED_QUERY_NAME,
//					User.QUERY_PARAM_NAME, action, em) == null) {
//				em.persist(newUser);
//			} else {
//				response.getWriter().println("username already exists");
//			}
//		} catch (Exception e) {
//			response.getWriter().println(e.getLocalizedMessage());
//		} finally {
//			em.close();
//		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = (String) request.getParameter("action");

		if (action == null) {
			return;
		}

		try {
			if (action.compareTo("login") == 0) {
				login(request, response);
			}
			if (action.compareTo("register") == 0) {
				register(request, response);
			}
		} catch (ServerException e) {

		} catch (UserException e) {

		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, IOException, UserException {
		LoginRequest req = null;
		LoginResponse res = new LoginResponse();

		try {
			req = jsonMapper.readValue(request.getInputStream(),
					LoginRequest.class);
		} catch (Exception e) {
			res.setError(true);
			res.setErrorMessage("Server got error!");
			throw new ServerException("Failed to parse LoginRequest.", e);
		}
		UserDAO user_dao = new UserDAO();
		
		if (user_dao.userExistsByEmail(req.getId())) {
			res.setError(true);
			res.setErrorMessage("Username not in database!");
			throw new UserException("Username not in database!");
		} else {
			String req_pw = req.getPassword();
			if (req_pw.compareTo(user_dao.getUserByEmail(req.getId()).getPassword()) == 0) {
				res.setError(false);
				res.setId(req.getId());
			} else {
				res.setError(true);
				res.setErrorMessage("Password does not match!");
				throw new UserException("Password does not match!");
			}
		}

		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			res.setError(true);
			res.setErrorMessage("Server got error!");
			throw new ServerException("Failed to parse LoginResponse to JSON.",	e);
		}

	}
	
	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, IOException, UserException {
		RegistrationRequest req = null;
		RegistrationResponse res = new RegistrationResponse();
		UserDAO user_dao = new UserDAO();
		
		try {
			req = jsonMapper.readValue(request.getInputStream(),
					RegistrationRequest.class);
		} catch (Exception e) {
			res.setError(true);
			res.setErrorMessage("Server got error!");
			throw new ServerException("Failed to parse LoginRequest.", e);
		}
		
		if (checkRegistrationInput(req, res))
		{
			user_dao.insertUser(req.getName(), req.getId(), req.getPassword());
		}
		
		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException("Failed to parse RegistrationResponse to JSON.",	e);
		}
	}
	
	private boolean checkRegistrationInput(RegistrationRequest req, Response res)
	{
		UserDAO userDao = new UserDAO();
		if (req.getPassword() == null || req.getPassword().equals(""))
		{
			res.setError(true);
			res.setErrorMessage("Password cannot be empty!");
			return false;
			
		}
		if (req.getId() == null || req.getId().equals(""))
		{
			res.setError(true);
			res.setErrorMessage("Email cannot be empty!");
			return false;
		}
		if (req.getName() == null || req.getName().equals(""))
		{
			res.setError(true);
			res.setErrorMessage("User name cannot be empty!");
			return false;
		}
		if (userDao.userExistsByEmail(req.getId()))
		{
	        res.setError(true);
	        res.setErrorMessage("User already exists!");
	        return false;
		}
		return true;
	}
}
