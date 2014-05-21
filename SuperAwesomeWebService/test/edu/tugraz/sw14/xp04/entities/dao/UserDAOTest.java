package edu.tugraz.sw14.xp04.entities.dao;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.tugraz.sw14.xp04.entities.User;

@RunWith(JUnit4.class)
public class UserDAOTest extends TestCase {

	private UserDAO userDao;
	
	private User user;

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		userDao = new UserDAO();
		user = new User();
		user.setEmail("user@gmail.com");
		user.setName("userName");
		user.setPassword("pw");
		
		userDao.insertUser(user.getName(), user.getEmail(), user.getPassword());
	}

	@Override
	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testInsert() {
		String name = "horstName";
		String email = "horst@slany.com";
		String pw = "horstPW";
		userDao.insertUser(name, email, pw);
		User user = userDao.getUserByEmail(email);
		assertEquals(name, user.getName());
		assertEquals(email, user.getEmail());
		assertEquals(pw, user.getPassword());
		assertEquals(null, user.getGcmId());
	}
	
	@Test
	public void testGetUserNotExisting() {
		String email = "horst@slany.com";
		User user = userDao.getUserByEmail(email);
		assertEquals(null, user);
	}
	
	@Test
	public void testNonExistingUserExists() {
		String email = "horst@slany.com";
		boolean found = userDao.userExistsByEmail(email);
		assertEquals(false, found);
	}
	
	@Test
	public void testUserExists() {
		boolean found = userDao.userExistsByEmail(user.getEmail());
		assertEquals(true, found);
	}
	
	@Test
	public void testUpdateGCMID() {
		boolean succeeded = userDao.updateGcmId(user.getEmail(), "abc");
		assertEquals(true, succeeded);
		User updatedUser = userDao.getUserByEmail(user.getEmail());
		assertEquals(user.getName(), updatedUser.getName());
		assertEquals(user.getEmail(), updatedUser.getEmail());
		assertEquals(user.getPassword(), updatedUser.getPassword());
		assertEquals("abc", updatedUser.getGcmId());
	}
	
	@Test
	public void testUpdateGCMIDOfNonExistingUser() {
		boolean succeeded = userDao.updateGcmId("naturRadler", "abc");
		assertEquals(false, succeeded);
	}	
		
}
