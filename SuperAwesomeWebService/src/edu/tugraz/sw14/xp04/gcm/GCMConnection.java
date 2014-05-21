package edu.tugraz.sw14.xp04.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tugraz.sw14.xp04.entities.User;
import edu.tugraz.sw14.xp04.stubs.GCMMessage;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;

public class GCMConnection {

	private HttpURLConnection connection;
	private final String API_KEY = "AIzaSyDaEIpAaziLsL9SVA5N3_MzPdf8FLVA7wA";
	private final ObjectMapper jsonMapper;

	public GCMConnection() {

		jsonMapper = new ObjectMapper();

	}

	public boolean sendMessage(SendMessageRequest request, String sender,
			String receiverGcmRegId) {
		URL url = null;

		try {
			url = new URL("https://android.googleapis.com/gcm/send");
		} catch (MalformedURLException e) {
			System.err.println("invalid url sendMessage");
			return false;
		}

		String line = "";
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "key=" + API_KEY);
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			GCMMessage msg = new GCMMessage();
			msg.setRegistration_ids(Arrays.asList(receiverGcmRegId));

			System.err.println(receiverGcmRegId);

			Map<String, String> data = new HashMap<String, String>();
			data.put("message", request.getMessage());
			data.put("sender", sender);
			msg.setData(data);

			OutputStream out = connection.getOutputStream();

			String jsonGCMMsg = jsonMapper.writeValueAsString(msg);

			System.err.println(jsonGCMMsg);

			out.write(jsonGCMMsg.getBytes());
			out.close();

			int status = connection.getResponseCode();
			if (status == 200) {
				InputStream is = connection.getInputStream();
				InputStreamReader isReader = new InputStreamReader(is);
				BufferedReader bReader = new BufferedReader(isReader);

				while ((line = bReader.readLine()) != null) {
					System.err.println(line);
				}
				System.err.println(line);
				if (line.contains("\"failure\":0")) {
					bReader.close();
					return false;
				}

				bReader.close();
			} else {
				System.err.println("Post failed with error code " + status);
				return false;
			}
		} catch (IOException e) {
			System.err.println("Post failed with error code " + e.getMessage());
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		if (line.contains("\"success\":1"))
			return true;
		else
			return false;
	}

}
