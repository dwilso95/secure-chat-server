package chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Builds {@link KeyStore}s of type key and trust.
 */
public class KeyStoreFactory {

	/**
	 * Private on purpose since this class will be 'static'
	 */
	private KeyStoreFactory() {
		// do nothing
	}

	public static final KeyStore createKeyStore(final ChatContext context) throws NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException {
		return createStore(context, "keystore");
	}

	public static final KeyStore createTrustStore(final ChatContext context) throws NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException {
		return createStore(context, "truststore");
	}

	private static final KeyStore createStore(final ChatContext context, final String type) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		final KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(context.getProperty(type)), context.getProperty("password").toCharArray());
		return keystore;
	}
}
