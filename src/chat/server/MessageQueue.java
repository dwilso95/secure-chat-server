package chat.server;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import chat.ChatMessage;
import chat.User;

/**
 * Singleton used for transferring {@link ChatMessage}s between
 * {@link ServerThread}s.
 * 
 * This 'queue' holds a {@link Queue} of {@link ChatMessage}s for each
 * registered {@link User}.
 *
 */
public final class MessageQueue {

	private static final MessageQueue INSTANCE = new MessageQueue();
	private static final Map<User, Queue<ChatMessage>> messageQueueByUser = new ConcurrentHashMap<>();

	private MessageQueue() {

	}

	/**
	 * "Registers" a {@link User}. This creates an empty {@link Queue} for tracking
	 * {@link ChatMessage} for the given {@link User}.
	 * 
	 * @param user
	 *            - {@link User} to register
	 */
	public void registerUser(final User user) {
		if (!messageQueueByUser.containsKey(user)) {
			final Queue<ChatMessage> messageQueue = new ConcurrentLinkedQueue<>();
			messageQueueByUser.put(user, messageQueue);
		}
	}

	/**
	 * Returns the {@link Queue} for the given {@link User}
	 * 
	 * @param user
	 *            - {@link User} for which to return a {@link Queue}
	 * @return - {@link Queue}
	 */
	public Queue<ChatMessage> getQueueForUser(final User user) {
		// just call register in case a queue does not exist. this will not have any bad
		// affects
		registerUser(user);
		return messageQueueByUser.get(user);
	}

	/**
	 * Adds the given {@link ChatMessage} to all {@link User}s other than the given
	 * sender
	 * 
	 * @param chatMessage
	 *            - {@link ChatMessage} to add
	 * @param sender
	 *            - {@link User} who's queue will not be added
	 */
	public void addMessageToQueues(final ChatMessage chatMessage, final User sender) {
		for (final Entry<User, Queue<ChatMessage>> entry : messageQueueByUser.entrySet()) {
			if (!entry.getKey().equals(sender)) {
				entry.getValue().add(chatMessage);
			}
		}
	}

	/**
	 * @return the only instance of {@link MessageQueue}
	 */
	public static MessageQueue getInstance() {
		return INSTANCE;
	}

}
