package chat.clearance;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class for holding a level of clearance
 */
@XmlRootElement
public class ClearanceLevel {

	private int level;

	/**
	 * Default constructor
	 */
	public ClearanceLevel() {
		this.level = 0;
	}

	/**
	 * Constructor for defining level
	 * 
	 * @param level
	 *            - level of clearance defined as an int from 1 to 4
	 */
	public ClearanceLevel(final int level) {
		validateLevel(level);
		this.level = level;
	}

	@XmlElement
	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
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
