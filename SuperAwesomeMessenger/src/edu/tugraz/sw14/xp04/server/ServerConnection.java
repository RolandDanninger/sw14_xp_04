package edu.tugraz.sw14.xp04.server;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.util.Log;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class ServerConnection {

	public static final String SERVER_URL = "http://vernal-verve-538.appspot.com/superawesomewebservice";

	private HttpClient httpClient = null;
	private final ObjectMapper jsonMapper;

	private static final String REQ_PARSE_FAILED = "Failed to parse request "
			+ "into JSON format.";
	private static final String REQ_SEND_FAILED = "Failed to send request "
			+ "to server.";
	private static final String RES_PARSE_FAILED = "Failed to parse response "
			+ "from JSON.";
	private static final String ENTITY_CREATION_FAILED = "Failed to create entity "
			+ "with JSON body.";

	public ServerConnection(String url) {
		this.httpClient = new DefaultHttpClient();
		this.jsonMapper = new ObjectMapper();
	}

	private HttpPost createHttpPost(String action, StringEntity httpEntity) {
		HttpPost httpPost = new HttpPost(SERVER_URL + "?action=" + action);
		httpPost.addHeader(new BasicHeader("Accept", "application/json"));
		httpPost.addHeader(new BasicHeader("Content-type", "application/json"));
		httpPost.setEntity(httpEntity);
		return httpPost;
	}

	public LoginResponse login(LoginRequest request)
			throws ServerConnectionException {
		String entityJson = "";
		try {
			entityJson = jsonMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new ServerConnectionException(REQ_PARSE_FAILED, e);
		}

		StringEntity httpEntity = null;
		try {
			httpEntity = new StringEntity(entityJson);
		} catch (UnsupportedEncodingException e) {
			throw new ServerConnectionException(ENTITY_CREATION_FAILED, e);
		}

		HttpPost httpPost = createHttpPost("login", httpEntity);

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (Exception e) {
			throw new ServerConnectionException(REQ_SEND_FAILED, e);
		}

		LoginResponse res = null;
		try {
			res = jsonMapper.readValue(httpResponse.getEntity().getContent(),
					LoginResponse.class);
		} catch (Exception e) {
			throw new ServerConnectionException(RES_PARSE_FAILED, e);
		}

		return res;
	}

	public RegistrationResponse register(RegistrationRequest request)
			throws ServerConnectionException {
		String entityJson = "";
		try {
			entityJson = jsonMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new ServerConnectionException(REQ_PARSE_FAILED, e);
		}

		StringEntity httpEntity = null;
		try {
			httpEntity = new StringEntity(entityJson);
		} catch (UnsupportedEncodingException e) {
			throw new ServerConnectionException(ENTITY_CREATION_FAILED, e);
		}

		HttpPost httpPost = createHttpPost("register", httpEntity);

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (Exception e) {
			throw new ServerConnectionException(REQ_SEND_FAILED, e);
		}

		RegistrationResponse res = null;
		try {
			res = jsonMapper.readValue(httpResponse.getEntity().getContent(),
					RegistrationResponse.class);
		} catch (Exception e) {
			throw new ServerConnectionException(RES_PARSE_FAILED, e);
		}

		return res;
	}

	public SendMessageResponse sendMessage(SendMessageRequest request)
			throws ServerConnectionException {
		String entityJson = "";
		try {
			entityJson = jsonMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new ServerConnectionException(REQ_PARSE_FAILED, e);
		}

		// entityJson = "{\"receiverId\":\"" + request.getReceiverId()
		// + "\", \"message\":\"" + request.getMessage() + "\"}";

		StringEntity httpEntity = null;
		try {
			httpEntity = new StringEntity(entityJson);
		} catch (UnsupportedEncodingException e) {
			throw new ServerConnectionException(ENTITY_CREATION_FAILED, e);
		}

		HttpPost httpPost = createHttpPost("send", httpEntity);

		HttpResponse httpResponse = null;
		try {
			Log.d("post_request", entityJson);
			httpResponse = httpClient.execute(httpPost);

		} catch (Exception e) {
			throw new ServerConnectionException(REQ_SEND_FAILED, e);
		}

		SendMessageResponse res = null;
		try {
			res = jsonMapper.readValue(httpResponse.getEntity().getContent(),
					SendMessageResponse.class);
		} catch (Exception e) {
			throw new ServerConnectionException(RES_PARSE_FAILED, e);
		}

		return res;
	}

	public AddContactResponse addContact(AddContactRequest request)
			throws ServerConnectionException {
		String entityJson = "";
		try {
			entityJson = jsonMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new ServerConnectionException(REQ_PARSE_FAILED, e);
		}

		StringEntity httpEntity = null;
		try {
			httpEntity = new StringEntity(entityJson);
		} catch (UnsupportedEncodingException e) {
			throw new ServerConnectionException(ENTITY_CREATION_FAILED, e);
		}

		HttpPost httpPost = createHttpPost("addcontact", httpEntity);

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (Exception e) {
			throw new ServerConnectionException(REQ_SEND_FAILED, e);
		}

		AddContactResponse res = null;
		try {
			res = jsonMapper.readValue(httpResponse.getEntity().getContent(),
					AddContactResponse.class);
		} catch (Exception e) {
			throw new ServerConnectionException(RES_PARSE_FAILED, e);
		}

		return res;
	}
}
