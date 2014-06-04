package edu.tugraz.sw14.xp04.helpers;

public class EncryptionSimple implements Encryption {

	private final int key;

	public EncryptionSimple(int key2) {

		this.key = key2;
	}

	@Override
	public String encrypt(String data) {

		String encrypted = "";

		for (char ch : data.toCharArray()) {
			encrypted += (char) (ch + key);
		}

		return encrypted;
	}

	@Override
	public String decrypt(String data) {

		String decrypted = "";

		for (char ch : data.toCharArray()) {
			decrypted += (char) (ch - key);
		}

		return decrypted;
	}

}
