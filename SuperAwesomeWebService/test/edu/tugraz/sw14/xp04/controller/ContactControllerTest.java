package edu.tugraz.sw14.xp04.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.tugraz.sw14.xp04.ServerException;
import edu.tugraz.sw14.xp04.controllers.ContactController;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;

public class ContactControllerTest extends TestCase {

	private ContactController controller;
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	private UserDAO daoMock;

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		daoMock = createMock(UserDAO.class);
		controller = new ContactController(daoMock);
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
		controller = null;
	}

	@Test
	public void testUserNotExists() throws ServerException, IOException {
		AddContactRequest request = new AddContactRequest();

		request.setId("blub@blub.com");

		expect(daoMock.getUserByEmail(request.getId())).andReturn(null);

		EasyMock.replay(daoMock);

		AddContactResponse response = controller.getContactInfo(request);

		EasyMock.verify(daoMock);

		Assert.assertTrue(response.isError());
		Assert.assertEquals(ErrorMessages.USER_DOES_NOT_EXISTS,
				response.getErrorMessage());
	}

	@Test
	public void testUserExists() throws ServerException, IOException {
		AddContactRequest request = new AddContactRequest();

		request.setId("blub@blub.com");

		User user = new User();
		user.setEmail("guy@sam.com");
		user.setName("Guy");

		expect(daoMock.getUserByEmail(request.getId())).andReturn(user);

		EasyMock.replay(daoMock);

		AddContactResponse response = controller.getContactInfo(request);

		EasyMock.verify(daoMock);

		Assert.assertFalse(response.isError());
		Assert.assertEquals(response.getErrorMessage(), null);
		Assert.assertEquals(user.getEmail(), response.getContact().getEmail());
		Assert.assertEquals(user.getName(), response.getContact().getName());
		Assert.assertEquals(null, response.getContact().getImgUrl());
	}
}
