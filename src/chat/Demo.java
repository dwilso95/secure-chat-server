package chat;

import java.io.File;

import chat.client.Client;
import chat.client.ClientContext;
import chat.server.Server;
import chat.server.ServerContext;

public class Demo {

	public static void main(String[] args) throws Exception {

		// run server
		Server server = new Server(new ServerContext(new File("contexts/server.context")));
		new Thread(server).start();
		System.out.println("Server started");

		// run clients...
		Client client1 = new Client(new ClientContext(new File("contexts/client1.context")));
		new Thread(client1).start();
		System.out.println("Client 1 started");

		Client client2 = new Client(new ClientContext(new File("contexts/client2.context")));
		new Thread(client2).start();
		System.out.println("Client 2 started");

		Client client3 = new Client(new ClientContext(new File("contexts/client3.context")));
		new Thread(client3).start();
		System.out.println("Client 3 started");

		while (true) {
			Thread.sleep(2000L);
			client1.write("message", 2);
		}

		// add certs

	}

}
