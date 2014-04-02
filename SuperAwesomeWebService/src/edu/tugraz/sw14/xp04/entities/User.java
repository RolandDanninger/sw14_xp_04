package edu.tugraz.sw14.xp04.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

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
	private Long key;
	
	private String name;
	private String password;
	private String email;
	
	public User() {}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
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
	
	public static User findSingleUser(String namedQuery, String param, String value, EntityManager em) {
		if(value == null || value == "") {
			return null;
		}
		
		Query query = em.createNamedQuery(namedQuery)
						.setParameter(param, value);
		
		@SuppressWarnings("unchecked")
		List<User> resultList = query.getResultList();
		
		if(!resultList.isEmpty()) {
			
			if(resultList.size() > 1) {
				// UNIQUNESS VIOLATION
			}
			
			return resultList.get(0);
		}
		
		return null;
	}
}
