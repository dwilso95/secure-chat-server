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
		Client client = new Client(new ClientContext(new File("contexts/client.context")));
		new Thread(client).start();
		System.out.println("Client started");
		// see what happens...

		// add certs

	}

}
