package neolith;

import java.io.Closeable;
import java.io.Flushable;
import java.net.HttpURLConnection;

/**
 *
 */
public class IOUtil {

  public static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ignored) {
      }
    }
  }

  public static void flushQuietly(Flushable flushable) {
    if (flushable != null) {
      try {
        flushable.flush();
      } catch (Exception ignored) {
      }
    }
  }

  public static void closeQuietly(HttpURLConnection urlConnection) {
    if (urlConnection != null) {
      urlConnection.disconnect();
    }
  }
}
