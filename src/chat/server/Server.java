package chat.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

	private ServerSocket socketServer;
	private ClearanceService clearanceService;
	private List<Thread> serverThreads = new ArrayList<>();

	public Server(final ServerContext serverContext) {
		try {
			final int port = Integer.parseInt(serverContext.getProperty("port"));
			this.clearanceService = new ClearanceService(new File(serverContext.getProperty("clearance.file")));
			this.socketServer = new ServerSocket(port);
		} catch (final Exception e) {
			throw new RuntimeException("A problem was encountered creating server", e);
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				final Socket socket = this.socketServer.accept();
				final Thread serverThread = new Thread(new ServerThread(socket, new MessageVerifier(clearanceService)));
				serverThread.start();
				serverThreads.add(serverThread);
			} catch (IOException e) {
				System.out.println("Problem creating server thread for client connection...");
				e.printStackTrace();
			}
		}
	}

	// private void logMessage(final ChatMessage chatMessage, final String userDn,
	// final LocalDateTime dateTime) {
	// log the message as xml...
	// }

}
