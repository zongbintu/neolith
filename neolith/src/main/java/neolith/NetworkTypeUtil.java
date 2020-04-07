package neolith;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.telephony.TelephonyManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络相关工具类
 */
public class NetworkTypeUtil {
  /**
   * 未连接
   */
  public static final int TYPE_NONE = 0;
  public static final int TYPE_WIFI = 1;
  public static final int TYPE_OPERATOR_2G = 2;
  public static final int TYPE_OPERATOR_3G = 3;
  public static final int TYPE_OPERATOR_4G = 4;
  public static final int TYPE_OPERATOR_5G = 5;
  /**
   * 未知
   */
  public static final int TYPE_UNKNOWN = 9;

  private NetworkTypeUtil() {
    throw new RuntimeException("No instance.");
  }

  /**
   * 当且仅当已经连接了Wifi类型的网络才返回true,反之返回false.
   * require permission @see {@link android.Manifest.permission#ACCESS_NETWORK_STATE}
   */
  public static boolean isConnectedWifi(Context context) {
    NetworkInfo info = getNetworkInfo(context);
    return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
  }

  /**
   * 是否运营商网络
   */
  public static boolean isOperator(Context context) {
    return is2G(context)
        || is3G(context)
        || is4G(context)
        || is5G(context);
  }

  /**
   * 是否2G网络
   */
  public static boolean is2G(Context context) {
    return getNetworkType(context) == TYPE_OPERATOR_2G;
  }

  /**
   * 是否3G网络
   */
  public static boolean is3G(Context context) {
    return getNetworkType(context) == TYPE_OPERATOR_3G;
  }

  /**
   * 是否4G网络
   */
  public static boolean is4G(Context context) {
    return getNetworkType(context) == TYPE_OPERATOR_4G;
  }

  /**
   * 是否5G网络
   */
  public static boolean is5G(Context context) {
    return getNetworkType(context) == TYPE_OPERATOR_5G;
  }

  /**
   * 获取网络类型
   *
   * require permission @see {@link android.Manifest.permission#ACCESS_NETWORK_STATE} and {@link
   * android.Manifest.permission#READ_PHONE_STATE}
   *
   * @param context {@link Context}
   * @return the network type
   * @see #TYPE_NONE
   * @see #TYPE_WIFI
   * @see #TYPE_OPERATOR_2G
   * @see #TYPE_OPERATOR_3G
   * @see #TYPE_OPERATOR_4G
   * @see #TYPE_OPERATOR_5G
   * @see #TYPE_UNKNOWN
   */
  public static int getNetworkType(Context context) {
    NetworkInfo info = getNetworkInfo(context);
    if (info == null) {
      return TYPE_UNKNOWN;
    }
    if (info.isConnected()) {
      int netType = info.getType();
      if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_WIMAX) {
        return TYPE_WIFI;
      } else if (netType == ConnectivityManager.TYPE_ETHERNET
          || netType == ConnectivityManager.TYPE_MOBILE) {
        return getOperatorNetwork(context);
      } else {
        return TYPE_UNKNOWN;
      }
    } else {
      return TYPE_NONE;
    }
  }

  /**
   * 获取运营商网络类型
   *
   * require permission @see {@link android.Manifest.permission#READ_PHONE_STATE}
   *
   * @param context {@link Context}
   * @return the network operator type
   * @see #TYPE_OPERATOR_2G
   * @see #TYPE_OPERATOR_3G
   * @see #TYPE_OPERATOR_4G
   * @see #TYPE_OPERATOR_5G
   * @see #TYPE_UNKNOWN
   */
  private static int getOperatorNetwork(Context context) {
    TelephonyManager telephonyManager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (context.checkPermission(Manifest.permission.READ_PHONE_STATE,
        android.os.Process.myPid(),
        Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
      switch (telephonyManager.getNetworkType()) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
        case TelephonyManager.NETWORK_TYPE_EDGE: // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA: // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_IDEN: // ~25 kbps
        case TelephonyManager.NETWORK_TYPE_GSM:
          return TYPE_OPERATOR_2G;
        case TelephonyManager.NETWORK_TYPE_EVDO_0: // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:// ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA: // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA: // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS: // ~ 400-7000 kbps
        case TelephonyManager.NETWORK_TYPE_EHRPD: // ~ 1-2 Mbps
        case TelephonyManager.NETWORK_TYPE_EVDO_B:// ~ 5 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPAP:// ~ 10-20 Mbps
        case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
          return TYPE_OPERATOR_3G;
        case TelephonyManager.NETWORK_TYPE_IWLAN:
        case TelephonyManager.NETWORK_TYPE_LTE: // ~ 10+ Mbps
          return TYPE_OPERATOR_4G;
        case TelephonyManager.NETWORK_TYPE_NR:
          return TYPE_OPERATOR_5G;
        default:
          return TYPE_UNKNOWN;
      }
    }
    return TYPE_UNKNOWN;
  }

  /**
   * 返回网络连接信息
   * require permission @see {@link android.Manifest.permission#ACCESS_NETWORK_STATE}
   */
  public static NetworkInfo getNetworkInfo(Context context) {
    if (context.checkPermission(Manifest.permission.ACCESS_NETWORK_STATE,
        android.os.Process.myPid(),
        Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
      ConnectivityManager cm =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      return cm.getActiveNetworkInfo();
    }
    return null;
  }

  /**
   * 获取ip地址
   */
  public static String getIp(Context context) {
    if (isConnectedWifi(context)) {
      return getWifiIp(context);
    }
    return getMobileIp();
  }

  /**
   * 移动网络ip
   */
  public static String getMobileIp() {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      if (null != networkInterfaces) {
        while (networkInterfaces.hasMoreElements()) {
          NetworkInterface networkInterface = networkInterfaces.nextElement();
          Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
          while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddress = inetAddresses.nextElement();
            if (!inetAddress.isLoopbackAddress()) {
              String ipAddress = inetAddress.getHostAddress().toUpperCase();
              boolean isIpv4 = ipAddress.indexOf(':') < 0;
              if (isIpv4) {
                return ipAddress;
              } else {
                int suffixIndex = ipAddress.indexOf('%');
                return suffixIndex < 0 ? ipAddress : ipAddress.substring(0, suffixIndex);
              }
            }
          }
        }
      }
    } catch (SocketException ignore) {
    }

    return null;
  }

  public static String getWifiIp(Context context) {
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    if (wifiInfo == null) {
      return null;
    }
    int ipAddress = wifiInfo.getIpAddress();
    return (ipAddress & 0xFF)
        + "."
        + ((ipAddress >> 8) & 0xFF)
        + "."
        + ((ipAddress >> 16) & 0xFF)
        + "."
        + (ipAddress >> 24 & 0xFF);
  }
}
