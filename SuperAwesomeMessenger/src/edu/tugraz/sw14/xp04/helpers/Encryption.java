package edu.tugraz.sw14.xp04.helpers;

public interface Encryption {

  
  String encrypt(String data) throws Exception;
  
  String decrypt(String data) throws Exception;

}
