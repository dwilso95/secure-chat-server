package chat.server;

public class ClearanceLevel {

	private int level;

	public ClearanceLevel(final int level) {
		validateLevel(level);
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	private void validateLevel(final int level) {
		if (level >= 0 && level <= 4) {
			return;
		} else {
			throw new IllegalArgumentException("Level is outside of allowable range [1-4]");
		}
	}

}
