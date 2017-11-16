package chat.server;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import chat.message.ChatMessage;

public final class MessageQueue {

	private static final MessageQueue INSTANCE = new MessageQueue();
	private static final Map<User, Queue<ChatMessage>> messageQueueByUser = new ConcurrentHashMap<>();

	private MessageQueue() {

	}

	public void registerUser(final User user) {
		if (!messageQueueByUser.containsKey(user)) {
			final Queue<ChatMessage> messageQueue = new ConcurrentLinkedQueue<>();
			messageQueueByUser.put(user, messageQueue);
		}
	}

	public Queue<ChatMessage> getQueueForUser(final User user) {
		// just call register in case a queue does not exist. this will not have any bad
		// affects
		registerUser(user);
		return messageQueueByUser.get(user);
	}

	public void addMessageToQueues(final ChatMessage chatMessage, final User sender) {
		for (final Entry<User, Queue<ChatMessage>> entry : messageQueueByUser.entrySet()) {
			if (!entry.getKey().equals(sender)) {
				entry.getValue().add(chatMessage);
			}
		}
	}

	public static MessageQueue getInstance() {
		return INSTANCE;
	}

}
