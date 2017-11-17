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

public class SignatureFactory {

	private SignatureFactory() {
		// do nothings
	}

	public static final Signature createSignatureForVerification(final PublicKey publicKey)
			throws InvalidKeyException, NoSuchAlgorithmException {
		final Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(publicKey);
		return signature;
	}

	public static final Signature createSignatureForSigning(final Context context)
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, InvalidKeyException {
		final KeyStore keystore = KeyStoreFactory.createKeyStore(context);
		final PrivateKey privateKey = (PrivateKey) keystore.getKey(context.getProperty("alias"),
				context.getProperty("password").toCharArray());
		System.out.println(privateKey);
		final Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(privateKey);
		return signature;
	}

}
