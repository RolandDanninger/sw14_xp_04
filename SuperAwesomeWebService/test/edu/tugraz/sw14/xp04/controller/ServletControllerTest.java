package edu.tugraz.sw14.xp04.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Assert;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.tugraz.sw14.xp04.controllers.ServletController;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class ServletControllerTest extends TestCase {

	private ServletController servletController;
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	
	@Before
	public void setUp() {
		helper.setUp();
		servletController = new ServletController();
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
		servletController = null;
	}
	
	@Test 
	public void testLoginSucceed() {
		
       RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");
		
		RegistrationResponse response = servletController.register(request);
		
		LoginRequest login_request = new LoginRequest();
		
		login_request.setId("a@gmail.com");
		login_request.setPassword("pw");
		login_request.setGcmId("1234");


	    LoginResponse login_response = servletController.login(login_request);
	    
	    Assert.assertFalse(login_response.isError());
	    Assert.assertNull(login_response.getErrorMessage());
	}
	
	@Test
	public void testWrongPassword() {
		RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");
		
		RegistrationResponse response = servletController.register(request);
		
		LoginRequest login_request = new LoginRequest();
		
		login_request.setId("a@gmail.com");
		login_request.setPassword("haha");
		login_request.setGcmId("1234");
		
		LoginResponse login_response = servletController.login(login_request);
		
		Assert.assertTrue(login_response.isError());
	    Assert.assertEquals(ErrorMessages.PASSWORD_IS_WRONG, login_response.getErrorMessage());
	}
	
	@Test
	public void testNonExistingName() {
		RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");
		
		RegistrationResponse response = servletController.register(request);
		
		LoginRequest login_request = new LoginRequest();
		
		login_request.setId("b@gmail.com");
		login_request.setPassword("pw");
		login_request.setGcmId("1234");
		
		LoginResponse login_response = servletController.login(login_request);
		
		Assert.assertTrue(login_response.isError());
	    Assert.assertEquals(ErrorMessages.USER_DOES_NOT_EXISTS, login_response.getErrorMessage());
	}
	
	@Test
	public void testRegistrationSucceed() {
		RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");
		
		RegistrationResponse response = servletController.register(request);
		
		Assert.assertFalse(response.isError());
		Assert.assertNull(response.getErrorMessage());	
	}

	
	@Test
	public void testEmptyPassword() {
		RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setName("wos onders");
		
		RegistrationResponse response = servletController.register(request);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.PASSWORD_IS_EMPTY, response.getErrorMessage());
	}
	
	@Test
	public void testEmptyName() {
		RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setPassword("pw");
		
		RegistrationResponse response = servletController.register(request);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.NAME_IS_EMPTY, response.getErrorMessage());
	}
	
	@Test
	public void testEmptyEmail() {
		RegistrationRequest request = new RegistrationRequest();
		
		request.setName("wos onders");
		request.setPassword("pw");
		
		RegistrationResponse response = servletController.register(request);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.EMAIL_IS_EMPTY, response.getErrorMessage());
	}
	
	@Test
	public void testUserExists() {
		UserDAO daoMock = createMock(UserDAO.class);
		servletController = new ServletController(daoMock);
		RegistrationRequest request = new RegistrationRequest();
		
		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");
		
		expect(daoMock.userExistsByEmail(request.getId())).andReturn(true);
		EasyMock.replay(daoMock);
		
		RegistrationResponse response = servletController.register(request);
		
		EasyMock.verify(daoMock);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.USER_ALREADY_EXISTS, response.getErrorMessage());
	}
	
	
	
}
