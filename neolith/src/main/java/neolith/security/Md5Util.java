package neolith.security;

import android.util.Log;
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
    }
    return str;
  }
}
