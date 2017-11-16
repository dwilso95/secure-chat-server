package chat.client;

import java.io.Closeable;
import java.io.IOException;

import javax.net.ssl.SSLSocket;

import chat.SSLFactory;
import chat.clearance.ClearanceLevel;
import chat.message.ChatMessage;
import chat.server.User;

public class Client implements Runnable, Closeable {

	private final SSLSocket socket;
	private MessageSender sender;
	private MessageReceiver receiver;
	private User user;

	public Client(final ClientContext clientContext) throws Exception {
		this.socket = SSLFactory.getSSLSocket(clientContext);
		this.sender = new MessageSender(this.socket);
		this.receiver = new MessageReceiver(this.socket);
		this.user = new User(clientContext.getProperty("dn"));
	}

	public final boolean hasMore() {
		return this.receiver.hasMore();
	}

	public final ChatMessage read() {
		return this.receiver.read();
	}

	public final void write(final String message, final int level) throws IOException {
		this.write(new ChatMessage(message, new ClearanceLevel(level), "signature", user.getUserDn()));
	}

	public final void write(final ChatMessage chatMessage) throws IOException {
		this.sender.send(chatMessage);
	}

	private void authenticate() {
		try {
			this.socket.startHandshake();
		} catch (IOException e) {
			throw new RuntimeException("Problem handshaking with server.", e);
		}
	}

	@Override
	public void run() {
		authenticate();
		this.sender.start();
		this.receiver.start();
	}

	@Override
	public void close() throws IOException {
		this.receiver.close();
		this.sender.close();
		this.socket.close();
	}

}
