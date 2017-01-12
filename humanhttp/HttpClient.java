package humanhttp;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import humanhttp.ParameterMap;

public class HttpClient {
	private static final String[] USER_AGENTS = {
		"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36", // Chrome
		"Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", // Android
		"Mozilla/5.0 (BlackBerry; U; BlackBerry 9900; en) AppleWebKit/534.11+ (KHTML, like Gecko) Version/7.1.0.346 Mobile Safari/534.11+", // BlackBerry
		"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1", // Firefox
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A" // Safari
	};

	private Random randomGenerator;
	private String userAgent;

	private HttpURLConnection openConnection(String url, String requestMethod) {
		try {
			HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

			System.out.println("Connected to: " + url);
			System.out.println("Using method: " + requestMethod);

			return connection;
		} catch (Exception exception) {
			// Our URLs are sure to be valid; ignore this catch.
			System.out.println("Exception when creating URL!");
		}

		return null;
	}

	private String readResponse(HttpURLConnection connection) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;

			while ((inputLine = input.readLine()) != null) {
				response.append(inputLine);
			}

			input.close();

			System.out.println("Got response code: " + connection.getResponseCode());
			System.out.println("Got response: " + response.toString());

			return response.toString();
		} catch (IOException exception) {
			System.out.println("Exception when reading response!");
		}

		return "";
	}

	public void selectUserAgent() {
		userAgent = USER_AGENTS[randomGenerator.nextInt(USER_AGENTS.length)];

		System.out.println("Selected User-Agent: " + userAgent);
	}

	public String post(String url, ParameterMap parameters) {
		HttpURLConnection connection = openConnection(url, "POST");

		try {
			// Enable, set, and send body
			connection.setDoOutput(true);
			DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
			stream.writeBytes(parameters.toEncodedString());
			stream.flush();
			stream.close();
		} catch (IOException exception) {
			System.out.println("Exception when sending POST data!");
		}

		return readResponse(connection);
	}

	public String post(String url) {
		HttpURLConnection connection = openConnection(url, "POST");
		return readResponse(connection);
	}

	public String get(String url, ParameterMap parameters) {
		HttpURLConnection connection = openConnection(url + "?" + parameters.toEncodedString(), "GET");
		return readResponse(connection);
	}

	public String get(String url) {
		HttpURLConnection connection = openConnection(url, "GET");
		return readResponse(connection);
	}

	public HttpClient() {
		randomGenerator = new Random();
		selectUserAgent();
	}
}
