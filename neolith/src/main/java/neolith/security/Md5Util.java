package neolith.security;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5处理
 */
public class Md5Util {

  public static String md5(String str) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(str.getBytes());
      byte[] b = md.digest();

      int i;

      StringBuilder buf = new StringBuilder("");
      for (byte aB : b) {
        i = aB;
        if (i < 0) {
          i += 256;
        }
        if (i < 16) {
          buf.append("0");
        }
        buf.append(Integer.toHexString(i));
      }
      str = buf.toString();
    } catch (Exception e) {
      Log.e("MD5", "md5出错", e);
      return null;
    }
    return str;
  }

  public static String md5(File file) {
    if (!file.isFile()) {
      return null;
    }
    MessageDigest digest = null;
    FileInputStream in = null;
    byte buffer[] = new byte[1024];
    int len;
    try {
      digest = MessageDigest.getInstance("MD5");
      in = new FileInputStream(file);
      while ((len = in.read(buffer, 0, 1024)) != -1) {
        digest.update(buffer, 0, len);
      }
      in.close();
    } catch (Exception e) {
      Log.e("MD5", "md5出错", e);
      return null;
    }
    BigInteger bigInt = new BigInteger(1, digest.digest());
    return bigInt.toString(16);
  }
}
