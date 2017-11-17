package chat.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import chat.ChatContext;
import chat.SSLFactory;
import chat.clearance.ClearanceService;

/**
 * Chat server for handling messages from some number of chat clients.
 * 
 * Spawns multiple threads for each client connection in order to be
 * non-blocking.
 * 
 */
public class Server implements Runnable {

	private final ChatContext serverContext;
	private final SSLServerSocket secureSocketServer;
	private final ClearanceService clearanceService;
	private final List<Thread> serverThreads = new ArrayList<>();

	/**
	 * Creates an instance using the given context
	 * 
	 * @param serverContext
	 *            - {@link ChatContext}
	 */
	public Server(final ChatContext serverContext) {
		try {
			this.clearanceService = new ClearanceService(new File(serverContext.getProperty("clearance.file")));
			this.serverContext = serverContext;
			this.secureSocketServer = SSLFactory.getSSLSocketServer(this.serverContext);
			this.secureSocketServer.setNeedClientAuth(true);
		} catch (final Exception e) {
			throw new RuntimeException("A problem was encountered creating server", e);
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				final SSLSocket socket = (SSLSocket) this.secureSocketServer.accept();
				final Thread serverThread = new ServerThread(socket, new MessageVerifier(this.clearanceService),
						this.serverContext);
				serverThread.start();
				this.serverThreads.add(serverThread);
			} catch (Exception e) {
				throw new RuntimeException("Problem creating server thread for client connection...", e);
			}
		}
	}

}
