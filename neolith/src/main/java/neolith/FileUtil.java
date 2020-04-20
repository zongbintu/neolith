package neolith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class FileUtil {

  private FileUtil() {
  }


  /**
   * 获取文件的MD5校验码
   *
   * @param filePath 文件路径
   * @return 文件的MD5校验码
   */
  public static byte[] getFileMD5(String filePath) {
    File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
    return getFileMD5(file);
  }
  /**
   * 获取文件的MD5校验码
   *
   * @param file 文件
   * @return 文件的MD5校验码
   */
  public static byte[] getFileMD5(File file) {
    if (file == null) return null;
    DigestInputStream dis = null;
    try {
      FileInputStream fis = new FileInputStream(file);
      MessageDigest md = MessageDigest.getInstance("MD5");
      dis = new DigestInputStream(fis, md);
      byte[] buffer = new byte[1024 * 256];
      while (dis.read(buffer) > 0) ;
      md = dis.getMessageDigest();
      return md.digest();
    } catch (NoSuchAlgorithmException | IOException e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeQuietly(dis);
    }
    return null;
  }
  /**
   * 获取文件前缀
   *
   * @param file 文件
   * @return 文件前缀
   */
  public static String getPrefix(File file) {
    if(null == file){
      return null;
    }
    return getPrefix(file.getName());
  }

  /**
   * 获取文件前缀
   *
   * @param fileName 文件
   * @return 文件前缀
   */
  public static String getPrefix(String fileName) {
    if(null == fileName){
      return null;
    }
    if(fileName.lastIndexOf(".")==-1){
      return "";
    }
    return fileName.substring(0,fileName.lastIndexOf("."));
  }

  /**
   * 获取文件扩展名
   *
   * @param file 文件
   * @return 文件扩展名
   */
  public static String getExtension(File file) {
    if(null == file){
      return null;
    }
    return getExtension(file.getName());
  }

  /**
   * 获取文件扩展名
   *
   * @param fileName 文件名
   * @return 文件扩展名
   */
  public static String getExtension(String fileName) {
    if (StringUtils.isSpace(fileName)) return fileName;
    int lastPoi = fileName.lastIndexOf('.');
    int lastSep = fileName.lastIndexOf(File.separator);
    if (lastPoi == -1 || lastSep >= lastPoi) return "";
    return fileName.substring(lastPoi + 1);
  }
}
