package chat.server;

import chat.message.ChatMessage;

public class MessageVerifier {

	final ClearanceService clearanceService;

	public MessageVerifier(final ClearanceService clearanceService) {
		this.clearanceService = clearanceService;
	}

	public boolean verifyMessage(final ChatMessage chatMessage) {
		final ClearanceLevel userClearanceLevel = clearanceService.getClearanceLevelForUser(chatMessage.getUserDn());
		final ClearanceLevel messageClearanceLevel = chatMessage.getClearanceLevel();

		if (userClearanceLevel.getLevel() >= messageClearanceLevel.getLevel()) {
			return true;
		} else {
			return false;
		}
	}

}
