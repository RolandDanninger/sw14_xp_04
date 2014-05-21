package edu.tugraz.sw14.xp04.entities.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import edu.tugraz.sw14.xp04.entities.User;

public class UserDAO {

	public UserDAO() {
	}

	public void insertUser(String name, String email, String password) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity newUser = new Entity("User", name);

		datastore.put(newUser);

		Transaction txn = datastore.beginTransaction();
		Key key = newUser.getKey();

		Entity user;
		try {
			user = datastore.get(key);
			user.setProperty("name", name);
			user.setProperty("email", email);
			user.setProperty("password", password);

			datastore.put(user);

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}

		txn.commit();
	}

	public boolean userExistsByEmail(String email) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				email);

		Query query = new Query("User").setFilter(emailFilter);
		PreparedQuery preparedQuery = datastore.prepare(query);

		Entity userEntity = preparedQuery.asSingleEntity();

		return (userEntity != null);
	}

	public User getUserByEmail(String email) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				email);

		Query query = new Query("User").setFilter(emailFilter);
		PreparedQuery preparedQuery = datastore.prepare(query);

		Entity userEntity = preparedQuery.asSingleEntity();

		if (userEntity == null) {
			return null;
		} else {
			return createUserFromEntity(userEntity);
		}
	}

	public boolean updateGcmId(String email, String gcmId) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				email);

		Query query = new Query("User").setFilter(emailFilter);
		PreparedQuery preparedQuery = datastore.prepare(query);

		Entity userEntity = preparedQuery.asSingleEntity();
		
		if (userEntity == null)
			return false;

		userEntity.setProperty("gcmId", gcmId);
		datastore.put(userEntity);
		return true;
	}

	private User createUserFromEntity(Entity userEntity) {

		Key key = userEntity.getKey();
		String name = (String) userEntity.getProperty("name");
		String email = (String) userEntity.getProperty("email");
		String password = (String) userEntity.getProperty("password");
		String gcmId = (String) userEntity.getProperty("gcmId");

		return new User(key, name, password, email, gcmId);
	}
}
