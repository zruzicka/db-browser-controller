package cz.zr.browser.service;

import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

@Service
public class CipherService {

  private static String SECRET_KEY_ALGORITHM = "AES";
  private static Charset TEXT_ENCODING = StandardCharsets.UTF_8;

  /** Token is applied while deriving SecretKeySpec. */
  private static String TOKEN = "CF7A0B20371D906E8D0919DCB1EB1A1E243693BA";

  private static final int KEY_SIZE = 256;
  private static final int KEY_CALCULATION_ITERATIONS = 65535;

  private final Cipher cipher;


  public CipherService() throws NoSuchPaddingException, NoSuchAlgorithmException {
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  }

  /**
   * Given text is encrypted based on given key and returned in Base64 encoding.
   * @param plainText
   * @param salt
   * @return
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws InvalidKeyException
   */
  public String encrypt(String plainText, String salt) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {
    SecretKeySpec secret = deriveSecretKey(salt);
    byte[] plainTextByte = plainText.getBytes(TEXT_ENCODING);
    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
    byte[] encryptedByte = cipher.doFinal(plainTextByte);
    return DatatypeConverter.printBase64Binary(encryptedByte);
  }

  /**
   * Given encrypted text is decoded from Base64 encoding and decrypted by given key.
   * @param encryptedText
   * @param salt
   * @return
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws InvalidKeyException
   */
  public String decrypt(String encryptedText, String salt) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {
    byte[] decoded = DatatypeConverter.parseBase64Binary(encryptedText);
    SecretKeySpec secret = deriveSecretKey(salt);
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
    byte[] decryptedByte = cipher.doFinal(decoded);
    return new String(decryptedByte, TEXT_ENCODING);
  }

  private SecretKeySpec deriveSecretKey(String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] saltBytes = salt.getBytes(TEXT_ENCODING);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
      TOKEN.toCharArray(),
      saltBytes,
      KEY_CALCULATION_ITERATIONS,
      KEY_SIZE
    );
    SecretKey secretKey = factory.generateSecret(spec);
    return new SecretKeySpec(secretKey.getEncoded(), SECRET_KEY_ALGORITHM);
  }

  public String getRandomSalt(){
    return String.valueOf(new Random().nextLong());
  }
}
