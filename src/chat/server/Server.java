package chat.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import chat.message.ChatMessage;

public class Server implements Runnable {

	private ServerSocket socketServer;
	private ClearanceService clearanceService;
	private List<ServerThread> serverThreads = new ArrayList<>();
	private Queue<ChatMessage> messageQueue = new ConcurrentLinkedQueue<>();

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
			try (final ServerThread serverThread = new ServerThread(this.socketServer.accept(), messageQueue,
					new MessageVerifier(clearanceService));) {
				serverThread.run();
				serverThreads.add(serverThread);
			} catch (IOException e) {
				// TODO log error once logging is set up!
				e.printStackTrace();
			}
		}
	}

	// private void logMessage(final ChatMessage chatMessage, final String userDn,
	// final LocalDateTime dateTime) {
	// log the message as xml...
	// }

}
