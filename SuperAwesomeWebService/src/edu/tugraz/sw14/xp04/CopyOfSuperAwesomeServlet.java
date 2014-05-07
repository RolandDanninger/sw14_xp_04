package edu.tugraz.sw14.xp04;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import edu.tugraz.sw14.xp04.entities.GCMRegistration;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.manager.EMFService;

public class CopyOfSuperAwesomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public CopyOfSuperAwesomeServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String action = (String) request.getParameter("action");
//		
//		String name = (String) request.getParameter("name");
//		
//		if(action == null) {
//			return;
//		}
//		
//		if(action.equalsIgnoreCase("reg")) {
//			register(name, response);
//		}
//		else if(action.equalsIgnoreCase("reggcm")) {
//			regGCM(name, response);
//		}
//		else if(action.equalsIgnoreCase("getgcm")) {
//			getGCM(name, response);
//		}
//		else {
//			response.getWriter().println("unsupported action: " + action);
//		}
		
	}

	private void register(String username, HttpServletResponse response)  throws IOException {
//		EntityManager em = EMFService.get().createEntityManager();
//
//		User newUser = new User();
//		newUser.setName(username);
//		
//		try {
//			
//			if(User.findSingleUser(User.NAMED_QUERY_NAME, User.QUERY_PARAM_NAME, username, em) == null) {
//				em.persist(newUser);
//			}
//			else {
//				response.getWriter().println("username already exists");
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			response.getWriter().println(e.getLocalizedMessage());
//		}
//		finally {
//			em.close();
//		}
	}
	
	private void getGCM(String username, HttpServletResponse response) throws IOException {
//		EntityManager em = EMFService.get().createEntityManager();
//		try {
//			GCMRegistration gcmRegistration = GCMRegistration.find(username, em);
//			
//			response.getWriter().println("email: " + gcmRegistration.getEmail());
//			response.getWriter().println("reg_id: " + gcmRegistration.getGcmRegistrationId());
//			response.getWriter().println("sid: " + gcmRegistration.getSessionId());
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			response.getWriter().println(e.getLocalizedMessage());
//		}
//		finally {
//			em.close();
//		}
	}
	
	private void regGCM(String username, HttpServletResponse response) throws IOException {
//		EntityManager em = EMFService.get().createEntityManager();
//		User u = null;
//		try {
//			u = User.findSingleUser(User.NAMED_QUERY_NAME, User.QUERY_PARAM_NAME, username, em);
//			
//			response.getWriter().println("username: " + u.getName());
//			
//			GCMRegistration gcmRegistration = new GCMRegistration();
//			gcmRegistration.setEmail(u.getName());
//			gcmRegistration.setGcmRegistrationId("asdadasdasdadasdasd1234");
//			em.persist(gcmRegistration);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			response.getWriter().println(e.getLocalizedMessage());
//		}
//		finally {
//			em.close();
//		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
