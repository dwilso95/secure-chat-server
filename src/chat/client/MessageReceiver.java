package chat.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable, Closeable {

	private final BufferedReader reader;
	private final String dn;

	public MessageReceiver(final Socket socket, final String dn) throws Exception {
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.dn = dn;
	}

	@Override
	public void run() {
		boolean runReceiver = true;

		while (runReceiver) {
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println("Client [" + dn + "] received: " + line);
				}
			} catch (IOException e) {
				runReceiver = false;
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}
}
