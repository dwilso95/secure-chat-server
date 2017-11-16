package chat.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import chat.message.ChatMessage;

public class MessageReceiver extends Thread implements Closeable {

	private final BufferedReader reader;
	private final Queue<ChatMessage> receivedMessages = new ConcurrentLinkedQueue<>();
	private boolean runReceiver = true;

	public MessageReceiver(final Socket socket) throws Exception {
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public final boolean hasMore() {
		return (null != this.receivedMessages.peek());
	}

	public final ChatMessage read() {
		return this.receivedMessages.poll();
	}

	@Override
	public void run() {
		while (this.runReceiver) {
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					final ChatMessage chatMessage = ChatMessage.fromXML(line);
					receivedMessages.add(chatMessage);
					System.out.println(chatMessage.getUserDn() + "@" + chatMessage.getClearanceLevel().getLevel()
							+ " says: " + chatMessage.getMessage());
				}
			} catch (IOException e) {
				runReceiver = false;
			}
		}
	}

	@Override
	public void close() throws IOException {
		this.runReceiver = false;
	}

}
