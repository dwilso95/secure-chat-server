package chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Context {

	private final Map<String, String> properties = new HashMap<>();

	public Context(final File contextLocation) throws IOException {
		try (final BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(contextLocation), Charset.forName("UTF-8")));) {
			String line;
			while ((line = reader.readLine()) != null) {
				final String key = line.substring(0, line.indexOf('='));
				final String value = line.substring(line.indexOf('=') + 1);
				properties.put(key, value);
			}
		}
	}

	public final void addProperty(final String key, final String value) {
		properties.put(key, value);
	}

	public final Map<String, String> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	public final String getProperty(final String key) {
		return properties.get(key);
	}

}
