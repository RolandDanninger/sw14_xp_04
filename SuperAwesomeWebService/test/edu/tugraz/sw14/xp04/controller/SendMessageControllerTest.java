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
import edu.tugraz.sw14.xp04.controllers.SendMessageController;
import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.entities.dao.UserDAO;
import edu.tugraz.sw14.xp04.gcm.GCMConnection;
import edu.tugraz.sw14.xp04.stubs.ErrorMessages;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class SendMessageControllerTest extends TestCase {

	private SendMessageController controller;
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	private GCMConnection gcmConMock;

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		gcmConMock = createMock(GCMConnection.class);
		controller = new SendMessageController(gcmConMock);
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
		controller = null;
	}

	@Test
	public void testSendMessageSucceed() {
		UserDAO daoMock = createMock(UserDAO.class);

		controller = new SendMessageController(gcmConMock, daoMock);

		User user = new User();
		user.setGcmId("1234");

		SendMessageRequest smRequest = new SendMessageRequest();

		String sender = "Silvio";
		smRequest.setReceiverId("cool_guy@sam.com");
		smRequest.setMessage("wayne");

		expect(daoMock.getByEmail(smRequest.getReceiverId())).andReturn(
				user);
		expect(daoMock.existsByEmail(smRequest.getReceiverId())).andReturn(
				true);
		expect(gcmConMock.sendMessage(smRequest, sender, user.getGcmId(), System.currentTimeMillis()))
				.andReturn(true);

		EasyMock.replay(gcmConMock);
		EasyMock.replay(daoMock);

		SendMessageResponse smResponse = controller.sendMessage(smRequest,
				sender);

		EasyMock.verify(daoMock);
		EasyMock.verify(gcmConMock);

		Assert.assertFalse(smResponse.isError());
		Assert.assertEquals(null, smResponse.getErrorMessage());

	}

	@Test
	public void testEmptyReceiver() throws ServerException, IOException {
		SendMessageRequest smRequest = new SendMessageRequest();

		smRequest.setReceiverId("");
		smRequest.setMessage("wayne");

		SendMessageResponse smResponse = controller.sendMessage(smRequest,
				"silvio");

		Assert.assertTrue(smResponse.isError());
		Assert.assertEquals(ErrorMessages.RECEIVER_IS_EMPTY,
				smResponse.getErrorMessage());
	}

	@Test
	public void testEmptyMessage() throws ServerException, IOException {
		SendMessageRequest smRequest = new SendMessageRequest();

		smRequest.setReceiverId("cool_guy@sam.com");
		smRequest.setMessage("");

		SendMessageResponse smResponse = controller.sendMessage(smRequest,
				"silvio");

		Assert.assertTrue(smResponse.isError());
		Assert.assertEquals(ErrorMessages.MESSAGE_IS_EMPTY,
				smResponse.getErrorMessage());
	}
}
