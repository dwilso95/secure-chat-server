package chat.server;

import java.io.File;

public class RunServer {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Cannot run server. Must provide one argument.");
		}
		
		System.out.println("Starting Server");

		new Server(new ServerContext(new File(args[0]))).run();
	}

}
