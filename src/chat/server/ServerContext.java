package chat.server;

import java.io.File;
import java.io.IOException;

import chat.Context;

public class ServerContext extends Context {

	public ServerContext(final File serverContextLocation) throws IOException {
		super(serverContextLocation);
	}

}
