package humanhttp;

import humanhttp.HttpClient;

public class Example {
	private HttpClient httpClient = new HttpClient();

	public String testGetRequest() {
		return httpClient.get("https://api.github.com/");
	}

	public String testGetRequestWithParameters() {
		ParameterMap parameters = new ParameterMap();
		parameters.put("state", "closed");

		return httpClient.get("https://api.github.com/repos/Avlyssna/HumanHTTP/issues", parameters);
	}

	public String testPostRequest() {
		return httpClient.post("https://api.github.com/");
	}

	public String testPostRequestWithParameters() {
		ParameterMap parameters = new ParameterMap();
		parameters.put("state", "closed");

		return httpClient.post("https://api.github.com/repos/Avlyssna/HumanHTTP/issues", parameters);
	}

	public String testGetRequestWithBaseUrl() {
		httpClient.setBaseUrl("https://api.github.com/");

		return httpClient.get("repos/Avlyssna/HumanHTTP/issues");
	}

	public String testGetRequestWithoutBaseUrl() {
		// We need to clear the baseUrl since we set it in testGetRequestWithBaseUrl
		httpClient.setBaseUrl();

		return httpClient.get("https://api.github.com/repos/Avlyssna/HumanHTTP/issues");
	}

	public Example() {
		System.out.println("Example got response: " + testGetRequest());
	}

	public static void main(String args[]) {
		new Example();
	}
}
