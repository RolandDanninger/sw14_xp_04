package edu.tugraz.sw14.xp04.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.tugraz.sw14.xp04.controllers.LoginController;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

@RunWith(JUnit4.class)
public class LoginControllerTest extends TestCase {

	private LoginController controller;
	private LoginRequest loginRequest;
	private User user;
	private UserDAO daoMock;
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		controller = new LoginController();

		loginRequest = new LoginRequest();

		loginRequest.setId("a@gmail.com");
		loginRequest.setPassword("pw");
		loginRequest.setGcmId("1234");

		user = new User();
		user.setPassword(loginRequest.getPassword());
		user.setEmail(loginRequest.getId());
		user.setGcmId(loginRequest.getGcmId());

		daoMock = createMock(UserDAO.class);
		controller = new LoginController(daoMock);
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
		controller = null;
		loginRequest = null;
		user = null;
		daoMock = null;
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testLoginSucceed() {

		expect(daoMock.userExistsByEmail(loginRequest.getId())).andReturn(true);
		expect(daoMock.getUserByEmail(loginRequest.getId())).andReturn(user);
		expect(
				daoMock.updateGcmId(loginRequest.getId(),
						loginRequest.getGcmId())).andReturn(true);

		EasyMock.replay(daoMock);

		LoginResponse login_response = controller.login(loginRequest);

		EasyMock.verify(daoMock);

		Assert.assertFalse(login_response.isError());
		Assert.assertNull(login_response.getErrorMessage());
	}

	@Test
	public void testWrongPassword() {

		loginRequest.setPassword("wrongPassword");

		expect(daoMock.userExistsByEmail(loginRequest.getId())).andReturn(true);
		expect(daoMock.getUserByEmail(loginRequest.getId())).andReturn(user);

		EasyMock.replay(daoMock);

		LoginResponse login_response = controller.login(loginRequest);

		EasyMock.verify(daoMock);

		Assert.assertTrue(login_response.isError());
		Assert.assertEquals(ErrorMessages.PASSWORD_IS_WRONG,
				login_response.getErrorMessage());
	}

	@Test
	public void testNonExistingUser() {
		expect(daoMock.userExistsByEmail(loginRequest.getId()))
				.andReturn(false);

		EasyMock.replay(daoMock);

		LoginResponse login_response = controller.login(loginRequest);

		EasyMock.verify(daoMock);

		Assert.assertTrue(login_response.isError());
		Assert.assertEquals(ErrorMessages.USER_DOES_NOT_EXISTS,
				login_response.getErrorMessage());
	}

	@Test
	public void testRequestIsNull() {

		loginRequest = null;

		exception.expect(IllegalStateException.class);
		LoginResponse login_response = controller.login(loginRequest);
	}
}
