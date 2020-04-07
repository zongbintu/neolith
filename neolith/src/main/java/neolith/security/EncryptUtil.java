package neolith.security;

import android.util.Log;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 *
 */
public class EncryptUtil {
  /**
   * RSA加密
   *
   * @param content 要加密的内容
   * @param encryptKey base64后的公钥
   * @return 加密后再base64的字符串
   */
  public static String encryptRSA(String content, String encryptKey) {
    byte[] encrypted = encryptRSA(content.getBytes(), Base64Util.decode(encryptKey));
    return Base64Util.encode(encrypted);
  }

  /**
   * RSA加密
   *
   * @param content 要加密的内容
   * @param encodedKey 公钥
   */
  public static byte[] encryptRSA(byte[] content, byte[] encodedKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
      PublicKey pubKey = keyFactory.generatePublic(keySpec);

      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      return cipher.doFinal(content);
    } catch (Exception e) {
      Log.e("Sign", "RSA加密出错:", e);
      return null;
    }
  }
}
