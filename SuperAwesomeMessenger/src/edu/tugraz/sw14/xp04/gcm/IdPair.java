package edu.tugraz.sw14.xp04.gcm;

import java.io.Serializable;

public class IdPair implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3226111403103029787L;
	private String gcmRegId;
	private String deviceServerId;

	public IdPair() {
		this.gcmRegId = null;
		this.deviceServerId = null;
	}

	public IdPair(String gcmRegId) {
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

}