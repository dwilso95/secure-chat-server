package chat.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import chat.message.ChatMessage;
import chat.server.ClearanceLevel;

public class Client implements Runnable, Closeable {

	private final Socket socket;
	private final PrintWriter writer;
	private MessageSender sender;
	private MessageReceiver receiver;
	private final String dn;

	public Client(final ClientContext clientContext) throws Exception {
		this.socket = new Socket(clientContext.getProperty("ip"), Integer.parseInt(clientContext.getProperty("port")));
		this.dn = clientContext.getProperty("dn");
		this.writer = new PrintWriter(this.socket.getOutputStream(), true);
		this.sender = new MessageSender(this.writer, this.dn);
		this.receiver = new MessageReceiver(this.socket, this.dn);
	}

	public final void write(final String message, final int level) throws IOException {
		this.sender.send(new ChatMessage(message, new ClearanceLevel(level), dn));
	}

	public final void write(final ChatMessage chatMessage) throws IOException {
		this.sender.send(chatMessage);
	}

	private void authenticate() {
		writer.println(dn);
	}

	@Override
	public void run() {
		authenticate();

		new Thread(this.sender).start();
		new Thread(this.receiver).start();
	}

	@Override
	public void close() throws IOException {
		this.sender.close();
		this.receiver.close();
		this.socket.close();
	}

}
