package chat.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import chat.SSLFactory;
import chat.clearance.ClearanceService;

public class Server implements Runnable {

	private final ServerContext serverContext;
	private final SSLServerSocket secureSocketServer;
	private final ClearanceService clearanceService;
	private final List<Thread> serverThreads = new ArrayList<>();

	public Server(final ServerContext serverContext) {
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
				final Thread serverThread = new Thread(
						new ServerThread(socket, new MessageVerifier(this.clearanceService), this.serverContext));
				serverThread.start();
				this.serverThreads.add(serverThread);
			} catch (IOException e) {
				throw new RuntimeException("Problem creating server thread for client connection...", e);
			}
		}
	}

}
