package edu.tugraz.sw14.xp04.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;

import com.google.appengine.api.datastore.Key;

@Entity
public class GCMRegistration {

	public GCMRegistration() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key deviceId;

	@Column(nullable = false)
	private String gcmRegistrationId;

	private String email;
	
	public Key getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Key deviceId) {
		this.deviceId = deviceId;
	}

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}
	
	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static GCMRegistration find(String email, EntityManager em) {
		
		Query query = em.createQuery("SELECT gcm FROM GCMRegistration gcm WHERE gcm.email = :email")
						.setParameter("email", email);
		
		@SuppressWarnings("unchecked")
		List<GCMRegistration> resultList = query.getResultList();
		
		if(!resultList.isEmpty()) {
			
			if(resultList.size() > 1) {
				// UNIQUNESS VIOLATION
			}
			
			return resultList.get(0);
		}
		
		return null;
	}
}
