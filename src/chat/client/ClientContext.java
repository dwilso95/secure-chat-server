package chat.client;

import java.io.File;
import java.io.IOException;

import chat.Context;

public class ClientContext extends Context {

	public ClientContext(final File clientContextLocation) throws IOException {
		super(clientContextLocation);
	}

}
