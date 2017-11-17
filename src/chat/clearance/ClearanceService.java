package chat.clearance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import chat.server.User;

/**
 * Handles {@link ClearanceLevel}s of {@link User}s.
 */
public class ClearanceService {

	private final Map<User, ClearanceLevel> clearanceLevelsForUsers = new ConcurrentHashMap<>();

	/**
	 * Creates a {@link ClearanceService} using the given {@link File}
	 * 
	 * File must be formatted as key=value.
	 * 
	 * @param clearanceFile
	 *            - {@link File} from which to load users and clearances
	 */
	public ClearanceService(final File clearanceFile) {
		loadClearanceLevels(clearanceFile);
	}

	/**
	 * Returns {@link ClearanceLevel} for the given {@link User}
	 * 
	 * @param user
	 *            - {@link User} for which to return {@link ClearanceLevel}
	 * @return - {@link ClearanceLevel}
	 */
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
					final String key = line.substring(0, line.lastIndexOf('='));
					final String value = line.substring(line.lastIndexOf('=') + 1);
					clearanceLevelsForUsers.put(new User(key), new ClearanceLevel(Integer.parseInt(value)));
				}
			} catch (IOException e) {
				throw new RuntimeException("Problem building ClearanceService from file: " + clearanceFile.getName(),
						e);
			}
		}
	}
}
