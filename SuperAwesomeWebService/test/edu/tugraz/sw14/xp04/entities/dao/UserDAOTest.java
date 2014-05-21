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

	private User userInDB;
	private User userNotInDB;

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Override
	@Before
	public void setUp() {
		helper.setUp();
		userDao = new UserDAO();
		userInDB = new User("success@gmail.com", "successName", "successPW");
		userNotInDB = new User("fail@gmail.com", "failName", "failPW");
		userDao.insert(userInDB);
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
		
		User u = new User(email, name, pw);
		userDao.insert(u);
		
		User user = userDao.getByEmail(email);
		
		assertEquals(name, user.getName());
		assertEquals(email, user.getEmail());
		assertEquals(pw, user.getPassword());
		assertEquals(null, user.getGcmId());
	}
	
	@Test
	public void testGetUserNotExisting() {
		User user = userDao.getByEmail(userNotInDB.getEmail());
		assertEquals(null, user);
	}
	
	@Test
	public void testNonExistingUserExists() {
		boolean found = userDao.existsByEmail(userNotInDB.getEmail());
		assertEquals(false, found);
	}
	
	@Test
	public void testUserExists() {
		boolean found = userDao.existsByEmail(userInDB.getEmail());
		assertEquals(true, found);
	}
	
	@Test
	public void testUpdateGCMID() {
		boolean succeeded = userDao.updateGcmId(userInDB.getEmail(), "abc");
		assertEquals(true, succeeded);
		User updatedUser = userDao.getByEmail(userInDB.getEmail());
		assertEquals(userInDB.getName(), updatedUser.getName());
		assertEquals(userInDB.getEmail(), updatedUser.getEmail());
		assertEquals(userInDB.getPassword(), updatedUser.getPassword());
		assertEquals("abc", updatedUser.getGcmId());
	}
	
	@Test
	public void testUpdateGCMIDOfNonExistingUser() {
		boolean succeeded = userDao.updateGcmId(userNotInDB.getEmail(), "abc");
		assertEquals(false, succeeded);
	}	
		
}
