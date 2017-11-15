package chat.server;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Queue;

import chat.message.ChatMessage;

public class ServerThread implements Runnable, Closeable {

	private static final long TIMEOUT = 60000L;
	private User user;
	private final Socket socket;
	private final MessageVerifier messageVerifier;
	private boolean runServerThread = true;

	public ServerThread(final Socket socket, final MessageVerifier messageVerifier) {
		this.socket = socket;
		this.messageVerifier = messageVerifier;
		this.user = new User("someDn");
	}

	public void run() {
		try (final PrintStream pstream = new PrintStream(socket.getOutputStream());
				final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			Long lastMessageTime = System.currentTimeMillis();

			authenticate(reader);

			while (runServerThread) {
				try {
					final Queue<ChatMessage> currentMessages = MessageQueue.getInstance().getQueueForUser(user);
					// while there are messages, write them
					while (currentMessages.peek() != null) {
						// check if this client is allowed to get message, then send
						final ChatMessage message = currentMessages.poll();
						if (messageVerifier.verifyMessage(message)) {
							pstream.println(message.getMessage());
							pstream.flush();
						}
					}
					String line;
					if (socket.getInputStream().available() > 0 && (line = reader.readLine()) != null) {
						lastMessageTime = System.currentTimeMillis();
						// build and verify message first
						// deserialize line for real
						MessageQueue.getInstance().addMessageToQueues(ChatMessage.fromXML(line));
					}
					if (System.currentTimeMillis() - lastMessageTime > TIMEOUT) {
						runServerThread = false;
					}
				} catch (IOException e) {
					e.printStackTrace();
					runServerThread = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			runServerThread = false;
		}
	}

	private void authenticate(final BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			this.user = new User(line);
			break;
		}
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

}
