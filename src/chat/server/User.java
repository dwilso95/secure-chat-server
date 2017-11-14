package chat.server;

public class User {

	private String userDn;
	private ClearanceLevel clearanceLevel;

	public User(final String userDn, final ClearanceLevel clearanceLevel) {
		this.userDn = userDn;
		this.clearanceLevel = clearanceLevel;
	}

	public ClearanceLevel getClearanceLevel() {
		return this.clearanceLevel;
	}

	public String getUserDn() {
		return this.userDn;
	}

}
