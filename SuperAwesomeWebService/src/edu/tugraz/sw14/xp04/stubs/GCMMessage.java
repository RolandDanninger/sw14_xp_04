package edu.tugraz.sw14.xp04.stubs;

import java.util.List;
import java.util.Map;

public class GCMMessage {

	private List<String> registration_ids;
	private Map<String, String> data;
	
	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public List<String> getRegistration_ids() {
		return registration_ids;
	}
	
	public Map<String, String> getData() {
		return data;
	}
}
