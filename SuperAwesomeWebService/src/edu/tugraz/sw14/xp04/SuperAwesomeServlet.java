package edu.tugraz.sw14.xp04;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.manager.EMFService;

public class SuperAwesomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public SuperAwesomeServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = (String) request.getParameter("action");
		
		if(action == null) {
			return;
		}
		
		response.getWriter().println("Action = " + action);
		
		EntityManager em = EMFService.get().createEntityManager();

		User newUser = new User();
		newUser.setName(action);
		
		try {
			
			if(User.findSingleUser(User.NAMED_QUERY_NAME, User.QUERY_PARAM_NAME, action, em) == null) {
				em.persist(newUser);
			}
			else {
				response.getWriter().println("username already exists");
			}
		}
		catch(Exception e) {
			response.getWriter().println(e.getLocalizedMessage());
		}
		finally {
			em.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
