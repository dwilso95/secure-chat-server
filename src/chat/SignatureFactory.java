package chat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Factory for easily creating SHA1withRSA {@link Signature}s for signing data
 * and verifying signed data.
 */
public class SignatureFactory {

	private SignatureFactory() {
		// do nothings
	}

	/**
	 * Creates a signature for verification using the given public key
	 * 
	 * @param publicKey
	 *            - key to use for verification
	 * @return {@link Signature}
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static final Signature createSignatureForVerification(final PublicKey publicKey)
			throws InvalidKeyException, NoSuchAlgorithmException {
		final Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(publicKey);
		return signature;
	}

	/**
	 * Creates a signature for signing data
	 * 
	 * @param context
	 *            - {@link ChatContext} for signature properties
	 * @return {@link Signature}
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidKeyException
	 */
	public static final Signature createSignatureForSigning(final ChatContext context)
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, InvalidKeyException {
		final KeyStore keystore = KeyStoreFactory.createKeyStore(context);
		final PrivateKey privateKey = (PrivateKey) keystore.getKey(context.getProperty("alias"),
				context.getProperty("password").toCharArray());
		final Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(privateKey);
		return signature;
	}

}
