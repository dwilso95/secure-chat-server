package chat.server;

import chat.clearance.ClearanceLevel;
import chat.clearance.ClearanceService;
import chat.message.ChatMessage;

public class MessageVerifier {

	final ClearanceService clearanceService;

	public MessageVerifier(final ClearanceService clearanceService) {
		this.clearanceService = clearanceService;
	}

	/**
	 * Verifies that the given message could be sent to the destinationUser
	 * 
	 * @param chatMessage
	 *            - message to verify
	 * @param destinationUser
	 *            - the user for which to check against
	 * @return boolean - true iff the destinationUser can receive this chatMessage,
	 *         else false
	 */
	public boolean verifyMessageDestination(final ChatMessage chatMessage, final User destinationUser) {
		if (chatMessage != null && destinationUser != null) {
			final ClearanceLevel userClearanceLevel = clearanceService.getClearanceLevelForUser(destinationUser);
			final ClearanceLevel messageClearanceLevel = chatMessage.getClearanceLevel();

			if (userClearanceLevel.getLevel() >= messageClearanceLevel.getLevel()) {
				return true;
			}
		}
		return false;
	}

	public boolean verifyMessageSource(final ChatMessage chatMessage, final User sourceUser) {
		if (chatMessage != null && sourceUser != null) {
			// verify signature
			return true;
		}
		return false;
	}

}
