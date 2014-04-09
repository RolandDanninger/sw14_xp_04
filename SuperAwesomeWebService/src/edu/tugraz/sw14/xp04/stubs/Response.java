package edu.tugraz.sw14.xp04.stubs;

public class Response {
	private boolean error;
	private String errorMessage;

	public Response() {
		super();
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
