package chat.server;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClearanceService {

	private Map<String, ClearanceLevel> clearanceLevelsForUsers = new ConcurrentHashMap<>();

	public ClearanceService(final File clearanceFile) {
		loadClearanceLevels(clearanceFile);
	}

	public ClearanceLevel getClearanceLevelForUser(final String userDn) {
		if (clearanceLevelsForUsers.containsKey(userDn)) {
			return clearanceLevelsForUsers.get(userDn);
		} else {
			throw new IllegalArgumentException("User is unknown: " + userDn);
		}
	}

	private void loadClearanceLevels(final File clearanceFile) {
		// see if it exists and is an actual directory
		if (clearanceFile.exists() && !clearanceFile.isDirectory()) {
			// try to read

		}
	}
}
