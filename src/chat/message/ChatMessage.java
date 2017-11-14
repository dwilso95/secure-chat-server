package chat.message;

import chat.server.ClearanceLevel;

public class ChatMessage {

	private String message;
	private ClearanceLevel clearanceLevel;
	private String userDn;

	public ChatMessage(final String message, final ClearanceLevel clearanceLevel, final String userDn) {
		this.message = message;
		this.clearanceLevel = clearanceLevel;
		this.setUserDn(userDn);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ClearanceLevel getClearanceLevel() {
		return clearanceLevel;
	}

	public void setClearanceLevel(ClearanceLevel clearanceLevel) {
		this.clearanceLevel = clearanceLevel;
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

}
