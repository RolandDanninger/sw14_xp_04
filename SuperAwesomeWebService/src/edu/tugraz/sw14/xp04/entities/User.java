package edu.tugraz.sw14.xp04.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.google.appengine.api.datastore.Key;

@Entity
@NamedQueries({
		@NamedQuery(
				name = User.NAMED_QUERY_NAME,
				query = "SELECT user FROM User user WHERE user.name = :name"
		),
		@NamedQuery(
				name = User.NAMED_QUERY_EMAIL,
				query = "SELECT user FROM User user WHERE user.email = :email"
		)
})

public class User {

	public static final String QUERY_PARAM_NAME = "name";
	public static final String QUERY_PARAM_EMAIL = "email";
	
	public static final String NAMED_QUERY_NAME = "findUserByName";
	public static final String NAMED_QUERY_EMAIL = "findUserByEmail";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private String name;
	private String password;
	private String email;
	private String gcmId;
	
	public User() {}

	public User(Key key, String name, String password, String email, String gcmId) {
		this.key = key;
		this.name = name;
		this.password = password;
		this.email = email;
		this.gcmId = gcmId;
	}

	public User(String email, String name, String password) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}
}
