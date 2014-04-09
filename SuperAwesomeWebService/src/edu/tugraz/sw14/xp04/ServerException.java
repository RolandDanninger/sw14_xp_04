package edu.tugraz.sw14.xp04;

public class ServerException extends Exception {

	private static final long serialVersionUID = -5569261788158313718L;

	public ServerException() {
		super();
	}

	public ServerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

}
