package edu.tugraz.sw14.xp04.helpers;


public class EncryptionSimple implements Encryption {

  private int key;

  EncryptionSimple(int key2) throws Exception {

    this.key = key2;
  }

  @Override
  public String encrypt(String data) throws Exception {

    String encrypted = "";
    
    for (char ch: data.toCharArray()) {
      encrypted += (char) (ch+key);
    }
    
    return encrypted;
  }

  @Override
  public String decrypt(String data) throws Exception {

    String decrypted = "";
    
    for (char ch: data.toCharArray()) {
      decrypted += (char) (ch-key);
    }
    
    return decrypted;
  }

}
