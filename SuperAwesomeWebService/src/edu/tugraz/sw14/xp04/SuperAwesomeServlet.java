package edu.tugraz.sw14.xp04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.controllers.ServletController;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;

public class SuperAwesomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper jsonMapper = null;

	public SuperAwesomeServlet() {
		this.jsonMapper = new ObjectMapper();
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
			else if (action.compareTo("register") == 0) {
				register(request, response);
			}
			else if (action.compareTo("send") == 0) {
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
			req = jsonMapper.readValue(request.getInputStream(), LoginRequest.class);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginRequest.", e);
		}
		
		ServletController controller = new ServletController();
		LoginResponse res = controller.login(req);
		
		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginResponse to JSON.",	e);
		}

	}
	
	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, IOException, UserException {
		RegistrationRequest req = null;
		
		try {
			req = jsonMapper.readValue(request.getInputStream(), RegistrationRequest.class);
		} catch (Exception e) {
			throw new ServerException("Failed to parse LoginRequest.", e);
		}
		
		ServletController controller = new ServletController();
		RegistrationResponse res = controller.register(req);
		
		try {
			jsonMapper.writeValue(response.getOutputStream(), res);
		} catch (Exception e) {
			throw new ServerException("Failed to parse RegistrationResponse to JSON.",	e);
		}
	}
	
	private void sendMessage(HttpServletRequest request, HttpServletResponse response) 
			throws ServerException, IOException {
		
//		SendMessageRequest
//		try {
//			req = jsonMapper.readValue(request.getInputStream(), RegistrationRequest.class);
//		} catch (Exception e) {
//			throw new ServerException("Failed to parse LoginRequest.", e);
//		}
//		
//		HttpClient client = new DefaultHttpClient();
//
//		HttpPost httpPost = new HttpPost("");
//		httpPost.addHeader("Authorization", "key=AIzaSyDaEIpAaziLsL9SVA5N3_MzPdf8FLVA7wA");
//		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		
//		params.add(new BasicNameValuePair("", ""));
//		params.add(new BasicNameValuePair("", ""));
//		params.add(new BasicNameValuePair("", ""));
//		
//		StringEntity entity = new StringEntity("JSONSTRING");
//		entity.setContentType("application/json");
//		httpPost.setEntity(entity);
//		
//		HttpResponse gcmResponse = client.execute(httpPost);
//		
//		RegistrationRequest r = new RegistrationRequest();
//		
//		r = jsonMapper.readValue(gcmResponse.getEntity().getContent(), RegistrationRequest.class);
//		
//		try {
////			jsonMapper.writeValue(response.getOutputStream(), res);
//		} catch (Exception e) {
//			throw new ServerException("Failed to parse RegistrationResponse to JSON.",	e);
//		}
	}
}
