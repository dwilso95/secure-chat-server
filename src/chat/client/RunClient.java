package chat.client;

import java.io.File;
import java.util.Scanner;

import chat.ChatContext;

/**
 * Main method for executing a chat {@link Client}
 */
public class RunClient {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException(
					"Cannot run client. Must provide one argument for the location of a chat context.");
		}

		try (final Client client = new Client(new ChatContext(new File(args[0])));) {
			new Thread(client).start();

			boolean runClient = true;
			try (Scanner scanner = new Scanner(System.in);) {

				while (runClient) {
					final String message = scanner.nextLine();
					if (message.equals("exit")) {
						runClient = false;
					} else {
						try {
							if (!message.isEmpty()) {
								client.write(message.substring(2), Integer.parseInt(message.substring(0, 1)));
							}
						} catch (Exception e) {
							// bad input, just continue
							System.err.println("Bad input. Must be in form: '${level}_${Message}'");
						}
					}
				}
			}
		}

	}

}
