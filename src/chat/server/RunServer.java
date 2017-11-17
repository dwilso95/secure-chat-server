package chat.server;

import java.io.File;

import chat.ChatContext;

/**
 * Runs a chat server using the supplied chat context location
 */
public class RunServer {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Cannot run server. Must provide one argument.");
		}

		System.out.println("Starting Server");

		new Server(new ChatContext(new File(args[0]))).run();
	}

}
