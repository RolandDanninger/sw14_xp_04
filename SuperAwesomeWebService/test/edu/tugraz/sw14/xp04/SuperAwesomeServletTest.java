package edu.tugraz.sw14.xp04;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import edu.tugraz.sw14.xp04.SuperAwesomeServlet;

public class SuperAwesomeServletTest extends TestCase {

	@SuppressWarnings("serial")
	public class ServletTest extends SuperAwesomeServlet {
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			super.doGet(request, response);
		}
		
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			super.doPost(request, response);
		}
	}
	
	private ServletTest servlet = new ServletTest();
	private HttpServletResponse responseMock;
	private HttpServletRequest requestMock;
	private PrintWriter printWriterMock;
	
	@Before
	public void setUp() {
		responseMock = createMock(HttpServletResponse.class);
		requestMock  = createMock(HttpServletRequest.class);
		
		printWriterMock = createMock(PrintWriter.class);
	}
	
	@Test
	public void testWrongAction() throws IOException, ServletException {
		
		expect(requestMock.getParameter("action")).andReturn("unkownAction");
		
		EasyMock.replay(requestMock);
			
		servlet.doPost(requestMock, responseMock);
		
		EasyMock.verify(requestMock);
	}
	
	@Test
	public void testEmptyRegistrationRequest() throws IOException, ServletException {

		// Fixture Setup
		
		// Request
		expect(requestMock.getParameter("action")).andReturn("register");
		expect(requestMock.getInputStream()).andReturn(null);
		
		// Response
		expect(responseMock.getOutputStream()).andReturn(null);
		printWriterMock.println("{\"error\":true,\"errorMessage\":\"no registration data\"}");
		expectLastCall();
		
		EasyMock.replay(requestMock);
		EasyMock.replay(responseMock);
		EasyMock.replay(printWriterMock);
			
		// Exercise
		servlet.doPost(requestMock, responseMock);
		
		// Verify
		EasyMock.verify(requestMock);
		EasyMock.verify(responseMock);
		EasyMock.verify(printWriterMock);
	}
	
	@Test
	public void testInvalidRegistrationRequest() throws IOException, ServletException {

		// Fixture Setup
		
		// Request
		expect(requestMock.getParameter("action")).andReturn("register");
		expect(requestMock.getParameter("registrationData")).andReturn("{\"horst\": true}");
		
		EasyMock.replay(requestMock);
		
		// Exercise
		servlet.doPost(requestMock, responseMock);
		
		// Verify
		EasyMock.verify(requestMock);
		
		//TODO: implement testcase and server logic
		fail();
	}
	
	@Test
	public void testRegistration() throws IOException, ServletException {

		// Fixture Setup
		
		// Request
		expect(requestMock.getParameter("action")).andReturn("register");
		//TODO: replace with real JSON
		expect(requestMock.getParameter("registrationData")).andReturn("{asdasdasdas}"); 
		
		// Response
		expect(responseMock.getWriter()).andReturn(printWriterMock);
		printWriterMock.println("{\"error\":false,\"errorMessage\":null}");
		expectLastCall();
		
		EasyMock.replay(requestMock);
		EasyMock.replay(responseMock);
		EasyMock.replay(printWriterMock);
			
		// Exercise
		servlet.doPost(requestMock, responseMock);
		
		// Verify
		EasyMock.verify(requestMock);
		EasyMock.verify(responseMock);
		EasyMock.verify(printWriterMock);
	}
	
}
