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

	private final Socket socket;
	private final Queue<ChatMessage> messageQueue;
	private final MessageVerifier messageVerifier;
	private boolean runServerThread = true;

	public ServerThread(final Socket socket, final Queue<ChatMessage> messageQueue,
			final MessageVerifier messageVerifier) {
		this.socket = socket;
		this.messageQueue = messageQueue;
		this.messageVerifier = messageVerifier;
	}

	public void run() {
		try (final PrintStream pstream = new PrintStream(socket.getOutputStream());
				final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			Long lastMessageTime = System.currentTimeMillis();

			while (runServerThread) {
				try {
					// while there are messages, write them

					while (messageQueue.peek() != null) {
						// check if this client is allowed to get message, then send
						final ChatMessage message = messageQueue.poll();
						if (messageVerifier.verifyMessage(message)) {
							pstream.println(message.getMessage());
						}
					}

					String line;
					while ((line = reader.readLine()) != null) {
						lastMessageTime = System.currentTimeMillis();
						// build and verify message first
						// deserialize line for real
						final ChatMessage message = new ChatMessage(line, new ClearanceLevel(2), "someDN");
						messageQueue.add(message);
					}

					if (System.currentTimeMillis() - lastMessageTime > 60000) {
						runServerThread = false;
					}
				} catch (IOException e) {
					System.out.println(e);
					runServerThread = false;
				}
			}
		} catch (IOException e) {
			System.out.println(e);
			runServerThread = false;
		}
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

}
