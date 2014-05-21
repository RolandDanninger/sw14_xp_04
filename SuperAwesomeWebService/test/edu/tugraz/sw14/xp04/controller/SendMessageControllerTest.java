package edu.tugraz.sw14.xp04.controller;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.tugraz.sw14.xp04.controllers.LoginController;
import edu.tugraz.sw14.xp04.controllers.RegistrationController;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class SendMessageControllerTest extends TestCase {

	private LoginController controller;
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		controller = new LoginController();
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
		controller = null;
	}

	@Test
	public void testLoginSucceed() {

		RegistrationRequest request = new RegistrationRequest();

		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");

		// TODO mock that shit
		RegistrationController regCon = new RegistrationController();
		RegistrationResponse response = regCon.register(request);

		LoginRequest login_request = new LoginRequest();

		login_request.setId("a@gmail.com");
		login_request.setPassword("pw");
		login_request.setGcmId("1234");

		LoginResponse login_response = controller.login(login_request);

		Assert.assertFalse(login_response.isError());
		Assert.assertNull(login_response.getErrorMessage());
	}

	@Test
	public void testWrongPassword() {
		RegistrationRequest request = new RegistrationRequest();

		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");

		// TODO mock that shit
		RegistrationController regCon = new RegistrationController();
		RegistrationResponse response = regCon.register(request);

		LoginRequest login_request = new LoginRequest();

		login_request.setId("a@gmail.com");
		login_request.setPassword("haha");
		login_request.setGcmId("1234");

		LoginResponse login_response = controller.login(login_request);

		Assert.assertTrue(login_response.isError());
		Assert.assertEquals(ErrorMessages.PASSWORD_IS_WRONG,
				login_response.getErrorMessage());
	}

	@Test
	public void testNonExistingName() {
		RegistrationRequest request = new RegistrationRequest();

		request.setId("a@gmail.com");
		request.setName("wos onders");
		request.setPassword("pw");

		RegistrationController regCon = new RegistrationController();
		RegistrationResponse response = regCon.register(request);

		LoginRequest login_request = new LoginRequest();

		login_request.setId("b@gmail.com");
		login_request.setPassword("pw");
		login_request.setGcmId("1234");

		LoginResponse login_response = controller.login(login_request);

		Assert.assertTrue(login_response.isError());
		Assert.assertEquals(ErrorMessages.USER_DOES_NOT_EXISTS,
				login_response.getErrorMessage());
	}
}
