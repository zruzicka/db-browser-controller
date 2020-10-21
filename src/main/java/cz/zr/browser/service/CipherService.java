package cz.zr.browser.service;

import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class CipherService {

  private static String HASH_ALGORITHM = "SHA-256";
  private static String ENCRYPTION_ALGORITHM = "AES";
  private static Charset TEXT_ENCODING = StandardCharsets.UTF_8;

  /** The key is applied for default SecretKey generation. */
  private static String CIPHER_SERVICE_KEY = "CF7A0B20371D906E8D0919DCB1EB1A1E243693BA";

  private static final int KEY_SIZE_IN_BYTES = 16;
  private static final int KEY_SIZE_IN_BITS = 128;

  private final Cipher cipher;


  public CipherService() throws NoSuchPaddingException, NoSuchAlgorithmException {
    cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
  }

  /**
   * Given text is encrypted based on given key and returned in Base64 encoding.
   * @param plainText
   * @param key
   * @return
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws InvalidKeyException
   */
  public String encrypt(String plainText, SecretKey key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
    byte[] plainTextByte = plainText.getBytes(TEXT_ENCODING);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedByte = cipher.doFinal(plainTextByte);
    return DatatypeConverter.printBase64Binary(encryptedByte);
  }

  /**
   * Given encrypted text is decoded from Base64 encoding and decrypted by given key.
   * @param encryptedText
   * @param key
   * @return
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws InvalidKeyException
   */
  public String decrypt(String encryptedText, SecretKey key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
    byte[] decoded = DatatypeConverter.parseBase64Binary(encryptedText);
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decryptedByte = cipher.doFinal(decoded);
    return new String(decryptedByte, TEXT_ENCODING);
  }

  /**
   * Generates random SecretKey.
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
    keyGenerator.init(KEY_SIZE_IN_BITS);
    SecretKey secretKey = keyGenerator.generateKey();
    return secretKey;
  }

  /**
   * Generates SecretKey based on givenKey.
   * @param givenKey
   * @return
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   */
  public static SecretKey createSecretKey(String givenKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    SecretKey secretKey;
    byte[] key = givenKey.getBytes(TEXT_ENCODING);
    MessageDigest sha = MessageDigest.getInstance(HASH_ALGORITHM);
    key = sha.digest(key);
    key = Arrays.copyOf(key, KEY_SIZE_IN_BYTES);
    secretKey = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
    return secretKey;
  }

  /**
   * Generates SecretKey based on default key defined in service.
   * @return
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   */
  public SecretKey createDefaultKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    return createSecretKey(CIPHER_SERVICE_KEY);
  }

}
