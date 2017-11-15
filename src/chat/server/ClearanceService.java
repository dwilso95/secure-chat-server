package chat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClearanceService {

	private Map<User, ClearanceLevel> clearanceLevelsForUsers = new ConcurrentHashMap<>();

	public ClearanceService(final File clearanceFile) {
		loadClearanceLevels(clearanceFile);
	}

	public ClearanceLevel getClearanceLevelForUser(final User user) {
		if (clearanceLevelsForUsers.containsKey(user)) {
			return clearanceLevelsForUsers.get(user);
		} else {
			throw new IllegalArgumentException("User is unknown: " + user.getUserDn());
		}
	}

	private void loadClearanceLevels(final File clearanceFile) {
		// see if it exists and is an actual directory
		if (clearanceFile.exists() && !clearanceFile.isDirectory()) {
			// try to read
			try (final BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(clearanceFile), Charset.forName("UTF-8")));) {
				String line;
				while ((line = reader.readLine()) != null) {
					final String key = line.substring(0, line.indexOf('='));
					final String value = line.substring(line.indexOf('=') + 1);
					clearanceLevelsForUsers.put(new User(key), new ClearanceLevel(Integer.parseInt(value)));
				}
			} catch (IOException e) {
				throw new RuntimeException("Problem building ClearanceService from file: " + clearanceFile.getName(),
						e);
			}
		}
	}
}
