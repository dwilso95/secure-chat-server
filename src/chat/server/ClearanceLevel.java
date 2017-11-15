package chat.server;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClearanceLevel {

	private int level;

	public ClearanceLevel() {
		this.level = 0;
	}

	public ClearanceLevel(final int level) {
		validateLevel(level);
		this.level = level;
	}

	@XmlElement
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

	@Override
	public String toString() {
		return String.valueOf(getLevel());
	}
}
