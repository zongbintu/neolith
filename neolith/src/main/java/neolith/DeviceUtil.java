package neolith;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 设备相关工具类
 */
public class DeviceUtil {
  private static String TAG = DeviceUtil.class.getName();
  private static final String[] SU_PATHS = {
      "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
      "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su",
      "/data/local/su"
  };

  private DeviceUtil() {
  }

  /**
   * 是否root
   *
   * @return 是否root
   */
  public static boolean isDeviceRooted() {
    return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
  }

  private static boolean checkRootMethod1() {
    String buildTags = android.os.Build.TAGS;
    return buildTags != null && buildTags.contains("test-keys");
  }

  private static boolean checkRootMethod2() {
    for (String path : SU_PATHS) {
      try {
        if (new File(path).exists()) return true;
      } catch (Exception ignore) {
      }
    }
    return false;
  }

  private static boolean checkRootMethod3() {
    BufferedReader in = null;
    try {
      java.lang.Process
          process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
      in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      process.destroy();
      return in.readLine() != null;
    } catch (Throwable ignore) {
    }
    try {
      if (in != null) {
        in.close();
      }
    } catch (IOException ignore) {
    }
    return false;
  }

  /**
   * 获取设备AndroidID
   *
   * @return AndroidID
   */
  public static String getAndroidID(Context context) {
    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  /**
   * 获取IMEI码
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
   *
   * @return IMEI码
   */
  public static String getIMEI(Context context) {
    if (context.checkPermission(Manifest.permission.READ_PHONE_STATE,
        android.os.Process.myPid(),
        Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager tm =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      if (tm != null) {
        try {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getImei();
          } else {
            return tm.getDeviceId();
          }
        } catch (SecurityException e) {
          Log.e(TAG, "device id error", e);
        }
      }
    }
    return null;
  }

  /**
   * 获取IMSI
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
   *
   * @return IMSI
   */
  public static String getIMSI(Context context) {
    if (context.checkPermission(Manifest.permission.READ_PHONE_STATE,
        android.os.Process.myPid(),
        Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager tm =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      try {
        return tm != null ? tm.getSubscriberId() : null;
      }catch (SecurityException e){
        Log.e(TAG,"getSubscriberId error",e);
      }
    }
    return null;
  }

  /**
   * 获取设备MAC地址
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
   *
   * @return MAC地址 默认返回 02:00:00:00:00:00
   */
  public static String getMacAddress(Context context) {
    String macAddress = getMacAddressByWifiInfo(context);
    if (!"02:00:00:00:00:00".equals(macAddress)) {
      return macAddress;
    }
    macAddress = getMacAddressByNetworkInterface();
    return macAddress;
  }

  /**
   * 获取设备MAC地址
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
   *
   * @return MAC地址
   */
  private static String getMacAddressByWifiInfo(Context context) {
    if (context.checkPermission(Manifest.permission.ACCESS_WIFI_STATE,
        android.os.Process.myPid(),
        Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      if (wifi != null) {
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) return info.getMacAddress();
      }
    }
    return "02:00:00:00:00:00";
  }

  /**
   * 获取设备MAC地址
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
   *
   * @return MAC地址
   */
  private static String getMacAddressByNetworkInterface() {
    try {
      Enumeration<NetworkInterface> networkInterfaceEnumeration =
          NetworkInterface.getNetworkInterfaces();
      if (null == networkInterfaceEnumeration) {
        return "02:00:00:00:00:00";
      }
      List<NetworkInterface> nis = Collections.list(networkInterfaceEnumeration);
      for (NetworkInterface ni : nis) {
        if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
        byte[] macBytes = ni.getHardwareAddress();
        if (macBytes != null && macBytes.length > 0) {
          StringBuilder res1 = new StringBuilder();
          for (byte b : macBytes) {
            res1.append(String.format("%02x:", b));
          }
          return res1.deleteCharAt(res1.length() - 1).toString();
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "mac address error", e);
    }
    return "02:00:00:00:00:00";
  }
}
