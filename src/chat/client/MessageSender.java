package chat.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import chat.message.ChatMessage;

/**
 * Thread which handles writing messages to a chat server over a socket
 */
public class MessageSender extends Thread implements Closeable {

	private final PrintWriter writer;
	private final Queue<ChatMessage> messagesToWrite = new ConcurrentLinkedQueue<>();
	private boolean runSender = true;

	/**
	 * Creates an instance which writes over the given socket
	 * 
	 * @param socket
	 * @throws Exception
	 */
	public MessageSender(final Socket socket) throws Exception {
		this.writer = new PrintWriter(socket.getOutputStream(), true);
	}

	/**
	 * Adds the given message to a queue which will be written to a chat server
	 * 
	 * @param chatMessage
	 */
	public void send(final ChatMessage chatMessage) {
		this.messagesToWrite.add(chatMessage);
	}

	@Override
	public void run() {
		try {
			while (this.runSender) {
				if (this.messagesToWrite.peek() != null) {
					final ChatMessage chatMessage = messagesToWrite.poll();
					// System.out.println("Client [" + dn + "] writing: " + chatMessage.getMessage()
					// + " with level: "
					// + chatMessage.getClearanceLevel());
					this.writer.println(ChatMessage.toXML(chatMessage));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		this.runSender = false;
	}

}
