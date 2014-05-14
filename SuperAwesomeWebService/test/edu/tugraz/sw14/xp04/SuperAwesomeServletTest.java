package edu.tugraz.sw14.xp04;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;

public class SuperAwesomeServletTest extends TestCase {

	@SuppressWarnings("serial")
	public class ServletTest extends SuperAwesomeServlet {
		@Override
		public void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			super.doGet(request, response);
		}

		@Override
		public void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			super.doPost(request, response);
		}
	}

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private final ServletTest servlet = new ServletTest();
	private HttpServletResponse responseMock;
	private HttpServletRequest requestMock;

	@Override
	@Before
	public void setUp() {
		helper.setUp();

		responseMock = createMock(HttpServletResponse.class);
		requestMock = createMock(HttpServletRequest.class);

		createMock(PrintWriter.class);
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testWrongAction() throws IOException, ServletException {

		expect(requestMock.getParameter("action")).andReturn("unkownAction");

		EasyMock.replay(requestMock);

		servlet.doPost(requestMock, responseMock);

		EasyMock.verify(requestMock);
	}

	@Test
	public void testRegistrationRequest() throws IOException, ServletException {
		// Fixture Setup
		final String email = "email@email.com";
		final String name = "asdf";
		final String password = "pw";

		RegistrationRequest request = new RegistrationRequest();
		request.setId(email);
		request.setName(name);
		request.setPassword(password);

		// Request
		expect(requestMock.getParameter("action")).andReturn("register");
		expect(requestMock.getInputStream()).andReturn(
				createInputStream(request));

		// Response
		expect(responseMock.getOutputStream()).andReturn(
				createEmptyServletOutputStream());

		EasyMock.replay(requestMock);
		EasyMock.replay(responseMock);

		// Exercise
		servlet.doPost(requestMock, responseMock);

		// Verify
		UserDAO dao = new UserDAO();
		Assert.assertTrue(dao.userExistsByEmail(email));
		EasyMock.verify(requestMock);
		EasyMock.verify(responseMock);
	}

	@Test
	public void testLoginRequest() throws IOException, ServletException {
		// Fixture Setup
		final String email = "email@email.com";
		final String password = "pw";

		LoginRequest request = new LoginRequest();
		request.setId(email);
		request.setPassword(password);

		// Request
		expect(requestMock.getParameter("action")).andReturn("login");
		expect(requestMock.getInputStream()).andReturn(
				createInputStream(request));

		// Response
		expect(responseMock.getOutputStream()).andReturn(
				createEmptyServletOutputStream());

		EasyMock.replay(requestMock);
		EasyMock.replay(responseMock);

		// Exercise
		servlet.doPost(requestMock, responseMock);

		// Verify
		EasyMock.verify(requestMock);
		EasyMock.verify(responseMock);
	}

	private ServletInputStream createInputStream(final String input) {
		return new ServletInputStream() {
			int i = 0;
			String json = input;

			@Override
			public int read() throws IOException {
				if (i >= json.length()) {
					return -1;
				}
				int charCode = json.charAt(i);
				i++;
				return charCode;
			}
		};
	}

	private ServletInputStream createInputStream(Object request) {
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = mapper.writeValueAsString(request);
			return createInputStream(json);
		} catch (JsonProcessingException e) {
		}

		fail("couldn't parse request");
		return null;
	}

	private ServletOutputStream createEmptyServletOutputStream() {
		return new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		};
	}
}
