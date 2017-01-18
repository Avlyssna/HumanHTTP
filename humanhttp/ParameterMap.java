package humanhttp;

import java.util.HashMap;
import java.util.Map;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

public class ParameterMap extends HashMap<Object, Object> {
	private String urlEncodeString(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException exception) {
			throw new UnsupportedOperationException(exception);
		}
	}

	public String toUrlEncodedString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (Map.Entry<Object, Object> entry : entrySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("&");
			}
			
			stringBuilder.append(
				String.format("%s=%s",
					urlEncodeString(entry.getKey().toString()),
					urlEncodeString(entry.getValue().toString())
				)
			);
		}

		return stringBuilder.toString();       
	}
}
