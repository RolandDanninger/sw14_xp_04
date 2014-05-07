package edu.tugraz.sw14.xp04.server;

public class ServerConnectionException extends Exception {

	private static final long serialVersionUID = 6515219597035675667L;

	public ServerConnectionException() {
		super();
	}

	public ServerConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ServerConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public ServerConnectionException(Throwable throwable) {
		super(throwable);
	}
	
}
