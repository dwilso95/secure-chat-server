package chat.server;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.Signature;
import java.time.ZonedDateTime;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.net.ssl.SSLSocket;

import chat.ChatContext;
import chat.ChatMessage;
import chat.SignatureFactory;
import chat.User;

/**
 * Thread which handles all communication for specific user's socket connection
 */
public class ServerThread extends Thread implements Runnable, Closeable {

	private static final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());

	static {
		try {
			final FileHandler fh = new FileHandler("ChatServer_" + ZonedDateTime.now().toEpochSecond() + ".log");
			fh.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e) {
			LOGGER.log(Level.WARNING, "Unable to log to file", e);
		}
	}

	private final long timeout;
	private final SSLSocket socket;
	private final MessageVerifier messageVerifier;
	private final BufferedReader reader;
	private final PrintStream writer;
	private User user;
	private Signature signature;
	private boolean runServerThread = true;

	/**
	 * Creates an instance for the given socket
	 * 
	 * @param socket
	 *            - {@link SSLSocket}
	 * @param messageVerifier
	 *            - {@link MessageVerifier}
	 * @param serverContext
	 *            - {@link ChatContext}
	 * @throws Exception
	 */
	public ServerThread(final SSLSocket socket, final MessageVerifier messageVerifier, final ChatContext serverContext)
			throws Exception {
		this.socket = socket;
		this.messageVerifier = messageVerifier;
		this.user = new User(socket.getSession().getPeerPrincipal().toString());
		this.timeout = Long.parseLong(serverContext.getProperty("timeout"));
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
		this.signature = SignatureFactory
				.createSignatureForVerification(socket.getSession().getPeerCertificateChain()[0].getPublicKey());
	}

	public void run() {
		// start receiver
		new Thread(new ReceiverThread()).start();

		// start sender
		new Thread(new SenderThread()).start();
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
		this.reader.close();
		this.writer.close();
	}

	class ReceiverThread implements Runnable, Closeable {

		public ReceiverThread() {
		}

		@Override
		public void run() {
			Long lastMessageTime = System.currentTimeMillis();
			while (runServerThread) {
				try {
					String line;
					if ((line = reader.readLine()) != null) {
						lastMessageTime = System.currentTimeMillis();
						LOGGER.info(line);
						final ChatMessage chatMessage = ChatMessage.fromXML(line);
						// we can check if the user could send this message by just checking if the user
						// could receive this message
						if (messageVerifier.verifyMessageSignature(chatMessage, signature)
								&& messageVerifier.verifyMessageDestination(chatMessage, user)) {
							MessageQueue.getInstance().addMessageToQueues(chatMessage, user);
						}
					}
					if (System.currentTimeMillis() - lastMessageTime > timeout) {
						runServerThread = false;
					}
				} catch (IOException e) {
					e.printStackTrace();
					runServerThread = false;
				}
			}
		}

		@Override
		public void close() throws IOException {
			reader.close();
		}
	}

	class SenderThread implements Runnable, Closeable {

		public SenderThread() {
		}

		@Override
		public void run() {
			final Queue<ChatMessage> currentMessages = MessageQueue.getInstance().getQueueForUser(user);

			while (runServerThread) {
				// while there are messages, write them
				while (currentMessages.peek() != null) {
					// check if this client is allowed to get message, then send
					final ChatMessage message = currentMessages.poll();
					if (messageVerifier.verifyMessageDestination(message, user)) {
						writer.println(ChatMessage.toXML(message));
						writer.flush();
					}
				}
			}
		}

		@Override
		public void close() throws IOException {
			writer.close();
		}

	}

}
