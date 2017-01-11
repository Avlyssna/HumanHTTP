package humanhttp;

import humanhttp.HttpClient;

public class UnitTest {
	private HttpClient httpClient;

	public static void main(String args[]) {
		new UnitTest();
	}

	public UnitTest() {
		httpClient = new HttpClient();
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("Key", "Value");

		httpClient.get("http://json.org/", parameterMap.toEncodedString());
	}
}