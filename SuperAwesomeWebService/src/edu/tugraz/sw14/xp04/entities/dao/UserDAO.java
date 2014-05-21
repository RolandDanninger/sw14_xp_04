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

	private static final String PROP_NAME = "name";
	private static final String PROP_EMAIL = "email";
	private static final String PROP_PW = "password";
	private static final String PROP_GCMID = "gcmId";
	private static final String ENTITY = "User";

	public UserDAO() {
	}

	public boolean insert(User u) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity newUser = new Entity(ENTITY, u.getName());

		datastore.put(newUser);

		Transaction txn = datastore.beginTransaction();
		Key key = newUser.getKey();

		Entity user;
		try {
			user = datastore.get(key);
			setEntityPropertiesFromUser(user, u);

			datastore.put(user);

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		txn.commit();
		return true;
	}

	public boolean existsByEmail(String email) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter emailFilter = new FilterPredicate(PROP_EMAIL,
				FilterOperator.EQUAL, email);

		Query query = new Query(ENTITY).setFilter(emailFilter);
		PreparedQuery preparedQuery = datastore.prepare(query);

		Entity userEntity = preparedQuery.asSingleEntity();

		return (userEntity != null);
	}

	public User getByEmail(String email) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter emailFilter = new FilterPredicate(PROP_EMAIL,
				FilterOperator.EQUAL, email);

		Query query = new Query(ENTITY).setFilter(emailFilter);
		PreparedQuery preparedQuery = datastore.prepare(query);

		Entity userEntity = preparedQuery.asSingleEntity();

		if (userEntity == null) {
			return null;
		} else {
			return createUserFromEntity(userEntity);
		}
	}

	private boolean update(String email, User u) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter emailFilter = new FilterPredicate(PROP_EMAIL,
				FilterOperator.EQUAL, email);

		Query query = new Query(ENTITY).setFilter(emailFilter);
		PreparedQuery preparedQuery = datastore.prepare(query);

		Entity userEntity = preparedQuery.asSingleEntity();

		if (userEntity == null)
			return false;

		setEntityPropertiesFromUser(userEntity, u);

		datastore.put(userEntity);
		return true;
	}

	public boolean updateGcmId(String email, String gcmId) {
		User u = getByEmail(email);
		if (u == null) 
			return false;
		
		u.setGcmId(gcmId);
		return update(email, u);
	}

	private User createUserFromEntity(Entity userEntity) {

		Key key = userEntity.getKey();
		String name = (String) userEntity.getProperty("name");
		String email = (String) userEntity.getProperty("email");
		String password = (String) userEntity.getProperty("password");
		String gcmId = (String) userEntity.getProperty("gcmId");

		return new User(key, name, password, email, gcmId);
	}

	private void setEntityPropertiesFromUser(Entity entity, User user) {
		entity.setProperty(PROP_NAME, user.getName());
		entity.setProperty(PROP_EMAIL, user.getEmail());
		entity.setProperty(PROP_PW, user.getPassword());
		entity.setProperty(PROP_GCMID, user.getGcmId());
	}
}
