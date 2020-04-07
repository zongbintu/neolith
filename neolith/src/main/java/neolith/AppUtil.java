package neolith;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 *
 */
public class AppUtil {
  private static String TAG = AppUtil.class.getName();

  private AppUtil() {
  }

  /**
   * 获取App版本码
   *
   * @param context 上下文
   * @return App版本码
   */
  public static long getVersionCode(Context context) {
    return getVersionCode(context, context.getPackageName());
  }

  /**
   * 获取App版本码
   *
   * @param context 上下文
   * @param packageName 包名
   * @return App版本码
   */
  public static long getVersionCode(Context context, String packageName) {
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      if (pi != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
          return pi.getLongVersionCode();
        } else {
          return pi.versionCode;
        }
      }
      return pi == null ? -1 : pi.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, String.format("page name %1s is not found", packageName), e);
      return -1;
    }
  }

  /**
   * 获取App版本号
   *
   * @param context 上下文
   * @return App版本号
   */
  public static String getAppVersionName(Context context) {
    return getAppVersionName(context, context.getPackageName());
  }

  /**
   * 获取App版本号
   *
   * @param context 上下文
   * @param packageName 包名
   * @return App版本号
   */
  public static String getAppVersionName(Context context, String packageName) {
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? null : pi.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, String.format("page name %1s is not found", packageName), e);
      return null;
    }
  }

  /**
   * 获取App名称
   *
   * @param context 上下文
   * @return App名称
   */
  public static String getAppName(Context context) {
    return getAppName(context, context.getPackageName());
  }

  /**
   * 获取App名称
   *
   * @param context 上下文
   * @param packageName 包名
   * @return App名称
   */
  public static String getAppName(Context context, String packageName) {
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, String.format("page name %1s is not found", packageName), e);
      return null;
    }
  }

  /**
   * 获取App具体设置的Intent
   *
   * @param packageName 包名
   * @return intent
   */
  public static Intent getAppDetailsSettingsIntent(String packageName) {
    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
    intent.setData(Uri.parse("package:" + packageName));
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  }
  public static Intent getHomeIntent() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addCategory(Intent.CATEGORY_HOME);
    return intent;
  }
}
