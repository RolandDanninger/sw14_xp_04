package edu.tugraz.sw14.xp04.helpers;

import org.junit.Ignore;

import junit.framework.TestCase;

public class EncryptionDESTest extends TestCase {

	private Encryption encryption;

	public EncryptionDESTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		encryption = new EncryptionDES();
	}

	@Override
	protected void tearDown() throws Exception {
	}

	public void testEncryptNotSame() throws Exception {
		String data = "i am a plain text";

		String data_encrypted = encryption.encrypt(data);

		assertFalse(data.equals(data_encrypted));
	}

	public void testDecryptNotSame() throws Exception {
		String data = "sakfjaslköfjsakldjfaslkfjslkffd";

		String data_decrypted = encryption.decrypt(data);

		assertFalse(data.equals(data_decrypted));
	}

	public void testEncryptAndDecrypt() throws Exception {
		String data = "~12_4hallo wie gehts dir'##?";

		String data_encrypted = encryption.encrypt(data);
		String data_decrypted = encryption.decrypt(data_encrypted);

		assertEquals(data, data_decrypted);
	}

}
