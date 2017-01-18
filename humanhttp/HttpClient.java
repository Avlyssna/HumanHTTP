package humanhttp;

import java.util.Random;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.UncheckedIOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.HttpURLConnection;
import java.net.URL;

import humanhttp.ParameterMap;

public class HttpClient {
	private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());
	private static final String[] USER_AGENTS = {
		"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36", // Chrome
		"Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", // Android
		"Mozilla/5.0 (BlackBerry; U; BlackBerry 9900; en) AppleWebKit/534.11+ (KHTML, like Gecko) Version/7.1.0.346 Mobile Safari/534.11+", // BlackBerry
		"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1", // Firefox
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A" // Safari
	};

	private Random randomGenerator = new Random();
	private String userAgent;
	private String baseUrl = "";

	// Internal methods
	private HttpURLConnection openConnection(String url, String requestMethod) {
		try {
			HttpURLConnection connection = (HttpURLConnection)(new URL(baseUrl + url)).openConnection();
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

			LOGGER.log(Level.INFO, String.format("Connected to: %s %s%s", requestMethod, baseUrl, url));

			return connection;
		} catch (MalformedURLException exception) {
			throw new RuntimeException(exception);
		} catch (IOException exception) {
			throw new UncheckedIOException(exception);
		}
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

			LOGGER.log(Level.INFO, "Got response code: " + connection.getResponseCode());
			LOGGER.log(Level.FINEST, "Got response: " + response.toString());

			return response.toString();
		} catch (IOException exception) {
			throw new UncheckedIOException(exception);
		}
	}

	// Public methods
	public synchronized String getUserAgent() {
		return userAgent;
	}

	public synchronized void setUserAgent(String userAgent) {
		this.userAgent = userAgent;

		LOGGER.log(Level.INFO, "Set User-Agent: " + getUserAgent());
	}

	public void setRandomUserAgent() {
		setUserAgent(USER_AGENTS[randomGenerator.nextInt(USER_AGENTS.length)]);
	}

	public synchronized String getBaseUrl() {
		return baseUrl;
	}

	public synchronized void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;

		LOGGER.log(Level.INFO, "Set base URL: " + getBaseUrl());
	}

	public void setBaseUrl() {
		setBaseUrl("");
	}

	public String get(String url, ParameterMap parameters) {
		return readResponse(
			openConnection(String.format("%s?%s", url, parameters.toUrlEncodedString()), "GET")
		);
	}

	public String get(String url) {
		return readResponse(openConnection(url, "GET"));
	}

	public String get() {
		return get("");
	}

	public String post(String url, ParameterMap parameters) {
		HttpURLConnection connection = openConnection(url, "POST");

		// Enable, set, and send body
		connection.setDoOutput(true);

		try {
			DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
			stream.writeBytes(parameters.toUrlEncodedString());
			stream.flush();
			stream.close();
		} catch (IOException exception) {
			throw new UncheckedIOException(exception);
		}

		return readResponse(connection);
	}

	public String post(String url) {
		return readResponse(openConnection(url, "POST"));
	}

	public String post() {
		return post("");
	}

	public HttpClient() {
		setRandomUserAgent();
	}
}
