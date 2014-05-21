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

import edu.tugraz.sw14.xp04.controllers.RegistrationController;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

@RunWith(JUnit4.class)
public class RegistrationControllerTest extends TestCase {

	private RegistrationController controller;
	private RegistrationRequest request;
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		controller = new RegistrationController();

		request = new RegistrationRequest();

		request.setId("a@gmail.com");
		request.setName("horst");
		request.setPassword("pw");
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
		controller = null;
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testRegistrationSucceed() {
		RegistrationResponse response = controller.register(request);

		Assert.assertFalse(response.isError());
		Assert.assertNull(response.getErrorMessage());
	}

	@Test
	public void testEmptyPassword() {
		request.setPassword("");

		RegistrationResponse response = controller.register(request);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.PASSWORD_IS_EMPTY,
				response.getErrorMessage());
	}

	@Test
	public void testEmptyName() {
		request.setName("");

		RegistrationResponse response = controller.register(request);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.NAME_IS_EMPTY,
				response.getErrorMessage());
	}

	@Test
	public void testEmptyEmail() {
		request.setId(null);

		RegistrationResponse response = controller.register(request);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.EMAIL_IS_EMPTY,
				response.getErrorMessage());
	}

	@Test
	public void testUserExists() {
		UserDAO daoMock = createMock(UserDAO.class);
		controller = new RegistrationController(daoMock);

		expect(daoMock.existsByEmail(request.getId())).andReturn(true);
		EasyMock.replay(daoMock);

		RegistrationResponse response = controller.register(request);

		EasyMock.verify(daoMock);
		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.USER_ALREADY_EXISTS,
				response.getErrorMessage());
	}

	@Test
	public void testRequestIsNull() {
		RegistrationRequest request = null;

		exception.expect(IllegalStateException.class);
		controller.register(request);
	}
}
