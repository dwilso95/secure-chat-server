package chat.server;

public class User {

	private String userDn;

	public User(final String userDn) {
		this.userDn = userDn;
	}

	public String getUserDn() {
		return this.userDn;
	}

	@Override
	public boolean equals(Object otherUser) {
		if (otherUser != null) {
			if (otherUser instanceof User) {
				return (((User) otherUser).getUserDn().equals(this.getUserDn()));
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.userDn.hashCode();
	}

	@Override
	public String toString() {
		return this.userDn;
	}
}
