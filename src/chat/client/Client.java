package chat.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable, Closeable {

	private final BufferedReader reader;
	private final DataOutputStream out;
	private final Socket socket;
	private boolean runClient = true;

	public Client(final ClientContext clientContext) throws Exception {
		this.socket = new Socket(clientContext.getProperty("ip"), Integer.parseInt(clientContext.getProperty("port")));
		this.out = new DataOutputStream(socket.getOutputStream());
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	int i = 0;

	public final void write() throws IOException {
		out.writeUTF("CHATTING " + i++);
		out.flush();
	}

	public final void read() throws IOException {
		System.out.println("!");
		String line = reader.readLine();
		System.out.println("!!!!");
		if (line != null && !line.isEmpty()) {
			System.out.println("Client " + line);
		}

	}

	@Override
	public void run() {
		System.out.println("Client started");
		while (runClient) {
			try {
				System.out.println("Client write");
				write();
				System.out.println("Client read");
				read();
			} catch (IOException e) {
				runClient = false;
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {
		this.out.close();
		this.reader.close();
		this.socket.close();
	}

}
