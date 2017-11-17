package chat.client;

import java.io.Closeable;
import java.io.IOException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import javax.net.ssl.SSLSocket;

import chat.ChatContext;
import chat.ChatMessage;
import chat.SSLFactory;
import chat.SignatureFactory;
import chat.User;
import chat.clearance.ClearanceLevel;

/**
 * Chat client. Requires a {@link ChatContext} which defines properties of the
 * client.
 * 
 * Client handles reading and writing data from/to Server.
 * 
 */
public class Client implements Runnable, Closeable {

	private final SSLSocket socket;
	private MessageSender sender;
	private MessageReceiver receiver;
	private User user;
	private Signature signature;

	/**
	 * Creates a Client using a {@link ChatContext} for all configuration
	 * information
	 * 
	 * @param clientContext
	 *            - {@link ChatContext}
	 * @throws Exception
	 */
	public Client(final ChatContext clientContext) throws Exception {
		this.signature = SignatureFactory.createSignatureForSigning(clientContext);
		this.socket = SSLFactory.getSSLSocket(clientContext);
		this.sender = new MessageSender(this.socket);
		this.receiver = new MessageReceiver(this.socket);
		this.user = new User(clientContext.getProperty("dn"));
	}

	/**
	 * Writes the given message at the given level to the chat server
	 * 
	 * @param message
	 *            - message to write
	 * @param level
	 *            - level at which to write the message
	 * @throws IOException
	 * @throws SignatureException
	 */
	public final void write(final String message, final int level) throws IOException, SignatureException {
		this.write(new ChatMessage(message, new ClearanceLevel(level), "signature", user.getUserDn()));
	}

	/**
	 * Writes the given message to the chat server
	 * 
	 * @param chatMessage
	 *            - the message to write to the server
	 * @throws IOException
	 * @throws SignatureException
	 */
	public final void write(final ChatMessage chatMessage) throws IOException, SignatureException {
		signature.update(chatMessage.getMessage().getBytes());
		final byte[] signatureBytes = signature.sign();
		final String sigatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);
		chatMessage.setSignature(sigatureBase64);
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
