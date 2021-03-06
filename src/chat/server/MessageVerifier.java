package chat.server;

import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import chat.ChatMessage;
import chat.User;
import chat.clearance.ClearanceLevel;
import chat.clearance.ClearanceService;

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

	/**
	 * Verifies the given message with the given {@link Signature}
	 * 
	 * @param chatMessage
	 *            - {@link ChatMessage} to verify
	 * @param signature
	 *            - {@link Signature} to use to verify
	 * @return - true iff the {@link Signature} verifies the {@link ChatMessage},
	 *         else false
	 */
	public boolean verifyMessageSignature(final ChatMessage chatMessage, final Signature signature) {
		if (chatMessage != null && signature != null) {
			// verify signature
			try {
				signature.update(chatMessage.getMessage().getBytes());
				return signature.verify(Base64.getDecoder().decode(chatMessage.getSignature()));
			} catch (SignatureException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
