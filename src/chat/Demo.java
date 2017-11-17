package chat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chat.client.Client;
import chat.server.Server;

public class Demo {

	public static void main(String[] args) throws Exception {

		// run server
		Server server = new Server(new ChatContext(new File("contexts/server.context")));
		new Thread(server).start();
		System.out.println("Server started");

		// run clients...
		Client client1 = new Client(new ChatContext(new File("contexts/client1.context")));
		new Thread(client1).start();
		System.out.println("Client 1 started");

		Client client2 = new Client(new ChatContext(new File("contexts/client2.context")));
		new Thread(client2).start();
		System.out.println("Client 2 started");

		Client client3 = new Client(new ChatContext(new File("contexts/client3.context")));
		new Thread(client3).start();
		System.out.println("Client 3 started");

		List<Client> clients = new ArrayList<>();
		clients.add(client1);
		clients.add(client2);
		clients.add(client3);

		while (true) {
			Thread.sleep(2000L);
			clients.get(new Random().nextInt(3)).write("secret message", 2);
		}

		// add certs

	}
}
