package chat.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import chat.message.ChatMessage;

/**
 * Thread for reading {@link ChatMessage}s from the chat server and prints it to
 * the console.
 */
public class MessageReceiver extends Thread implements Closeable {

	private final BufferedReader reader;
	private boolean runReceiver = true;

	/**
	 * Creates an instance which reads from the given socket
	 * 
	 * @param socket
	 * @throws Exception
	 */
	public MessageReceiver(final Socket socket) throws Exception {
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		while (this.runReceiver) {
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					final ChatMessage chatMessage = ChatMessage.fromXML(line);
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
