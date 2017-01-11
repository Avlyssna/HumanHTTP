package humanhttp;

import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ParameterMap extends HashMap<Object, Object> {
	private String urlEncodeUTF8(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException exception) {
			throw new UnsupportedOperationException(exception);
		}
	}

	public String toEncodedString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (Map.Entry<Object, Object> entry: entrySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("&");
			}
			
			stringBuilder.append(String.format("%s=%s",
				urlEncodeUTF8(entry.getKey().toString()),
				urlEncodeUTF8(entry.getValue().toString())
			));
		}

		return stringBuilder.toString();       
	}
}
