package chat.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import chat.message.ChatMessage;

public class MessageSender implements Runnable, Closeable {

	private final PrintWriter writer;
	private final String dn;
	private final Queue<ChatMessage> messagesToWrite = new ConcurrentLinkedQueue<>();

	public MessageSender(final PrintWriter writer, final String dn) throws Exception {
		this.writer = writer;
		this.dn = dn;
	}

	public void send(final ChatMessage chatMessage) {
		messagesToWrite.add(chatMessage);
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (messagesToWrite.peek() != null) {
					final ChatMessage message = messagesToWrite.poll();
					System.out.println("Client [" + dn + "] writing: " + ChatMessage.toXML(message));
					writer.println(ChatMessage.toXML(message));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
