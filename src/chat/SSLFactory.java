package chat;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLFactory {

	/**
	 * Private on purpose since this class will be 'static'
	 */
	private SSLFactory() {
		// do nothing
	}

	public static final SSLServerSocket getSSLSocketServer(final Context context) throws Exception {
		final SSLServerSocketFactory secureServerSocketFactory = getSSLContext(context).getServerSocketFactory();
		return (SSLServerSocket) secureServerSocketFactory
				.createServerSocket(Integer.parseInt(context.getProperty("port")));
	}

	public static final SSLSocket getSSLSocket(final Context context) throws Exception {
		final SSLSocketFactory ssf = getSSLContext(context).getSocketFactory();
		return (SSLSocket) ssf.createSocket(context.getProperty("ip"), Integer.parseInt(context.getProperty("port")));
	}

	private static final SSLContext getSSLContext(final Context context) throws Exception {
		final KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(context.getProperty("keystore")),
				context.getProperty("password").toCharArray());

		final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keystore, context.getProperty("password").toCharArray());

		final KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(new FileInputStream(context.getProperty("truststore")),
				context.getProperty("password").toCharArray());

		final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
		trustManagerFactory.init(trustStore);

		final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
		return sslContext;
	}

}
