package chat.client;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Closeable {

	// final DataInputStream in;
	private final DataOutputStream out;
	private final Socket socket;

	public Client(final ClientContext clientContext) throws Exception {
		this.socket = new Socket(clientContext.getProperty("ip"), Integer.parseInt(clientContext.getProperty("port")));
		out = new DataOutputStream(socket.getOutputStream());
	}

	int i = 0;

	public final void write() throws IOException {
		out.writeUTF("CHATTING " + i++);
	}

	@Override
	public void close() throws IOException {
		this.out.close();
		this.socket.close();
	}

}
