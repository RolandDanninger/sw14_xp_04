package edu.tugraz.sw14.xp04.helpers;

import java.io.Serializable;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3226111403103029787L;

	private String gcmRegId;
	private String deviceServerId;
	private String email;

	public UserInfo() {
		this.gcmRegId = null;
		this.deviceServerId = null;

	}

	public UserInfo(String gcmRegId) {
		this.gcmRegId = gcmRegId;
		this.deviceServerId = null;
	}

	public String getGcmRegId() {
		return gcmRegId;
	}

	public void setGcmRegId(String gcmRegId) {
		this.gcmRegId = gcmRegId;
	}

	public String getDeviceServerId() {
		return deviceServerId;
	}

	public void setDeviceServerId(String deviceServerId) {
		this.deviceServerId = deviceServerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmailo(String email) {
		this.email = email;
	}

}