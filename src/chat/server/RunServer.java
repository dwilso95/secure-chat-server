package chat.server;

import java.io.File;

public class RunServer {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Cannot run server. Must provide one argument.");
		}

		new Server(new ServerContext(new File(args[0])));
	}

}
