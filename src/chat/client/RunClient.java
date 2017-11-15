package chat.client;

import java.io.File;

public class RunClient {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Cannot run client. Must provide one argument.");
		}

		try (Client client = new Client(new ClientContext(new File(args[0])));) {
			new Thread(client).start();
		}

		boolean runClient = true;

		while (runClient) {

		}

	}

}
