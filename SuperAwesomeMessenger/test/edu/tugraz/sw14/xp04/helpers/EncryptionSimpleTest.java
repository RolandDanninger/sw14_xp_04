package edu.tugraz.sw14.xp04.helpers;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.TestCase;

public class EncryptionSimpleTest extends TestCase {

  private final int key = 3;
  private Encryption encryption;

  public EncryptionSimpleTest() {
    super();
  }

  @Override
  protected void setUp() throws Exception {

    encryption = new EncryptionSimple(key);
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
    String data = "i am a plain text";

    String data_encrypted = encryption.encrypt(data);
    String data_decrypted = encryption.decrypt(data_encrypted);

    assertEquals(data, data_decrypted);
  }

  public void testEncrypt() throws Exception {
    String data = "i am a plain text";

    String data_encrypted_expected = "";

    for (char ch : data.toCharArray()) {
      data_encrypted_expected += (char) (ch + key);
    }

    String data_encrypted = encryption.encrypt(data);

    assertEquals(data_encrypted_expected, data_encrypted);
  }

  public void testDecrypt() throws Exception {

    String data = "i am a plain text";

    String data_encrypted = "";

    for (char ch : data.toCharArray()) {
      data_encrypted += (char) (ch + key);
    }

    String data_decrypted = encryption.decrypt(data_encrypted);

    assertEquals(data, data_decrypted);
  }
}
