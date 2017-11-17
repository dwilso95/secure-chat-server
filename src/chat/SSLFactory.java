package chat;

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

	/**
	 * Generates a {@link SSLServerSocket} using the supplied context
	 * 
	 * @param context
	 *            - {@link ChatContext}
	 * @return {@link SSLServerSocket}
	 * @throws Exception
	 */
	public static final SSLServerSocket getSSLSocketServer(final ChatContext context) throws Exception {
		final SSLServerSocketFactory secureServerSocketFactory = getSSLContext(context).getServerSocketFactory();
		return (SSLServerSocket) secureServerSocketFactory
				.createServerSocket(Integer.parseInt(context.getProperty("port")));
	}

	/**
	 * Generates a {@link SSLSocket} using the supplied context
	 * 
	 * @param context
	 *            - {@link ChatContext}
	 * @return {@link SSLSocket}
	 * @throws Exception
	 */
	public static final SSLSocket getSSLSocket(final ChatContext context) throws Exception {
		final SSLSocketFactory sslSocketFactory = getSSLContext(context).getSocketFactory();
		return (SSLSocket) sslSocketFactory.createSocket(context.getProperty("ip"),
				Integer.parseInt(context.getProperty("port")));
	}

	private static final SSLContext getSSLContext(final ChatContext context) throws Exception {
		final KeyStore keystore = KeyStoreFactory.createKeyStore(context);
		final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keystore, context.getProperty("password").toCharArray());

		final KeyStore trustStore = KeyStoreFactory.createTrustStore(context);
		final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
		trustManagerFactory.init(trustStore);

		final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
		return sslContext;
	}

}
