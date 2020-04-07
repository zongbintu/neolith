package com.neolith.sample;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import neolith.AppUtil;
import neolith.DeviceUtil;
import neolith.NetworkTypeUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textView = findViewById(R.id.textview);

    findViewById(R.id.network_btn).setOnClickListener(this);
    findViewById(R.id.setting_btn).setOnClickListener(this);
    findViewById(R.id.home_btn).setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.network_btn:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          requestPermissions(new String[] {
              Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE
          }, 111);
        }
        StringBuilder sb = new StringBuilder("网络连接信息：");
        String type = "";
        switch (NetworkTypeUtil.getNetworkType(this)) {
          case NetworkTypeUtil.TYPE_NONE:
            type = "没有网络连接";
            break;
          case NetworkTypeUtil.TYPE_WIFI:
            type = "WIFI";
            break;
          case NetworkTypeUtil.TYPE_OPERATOR_2G:
            type = "2G";
            break;
          case NetworkTypeUtil.TYPE_OPERATOR_3G:
            type = "3G";
            break;
          case NetworkTypeUtil.TYPE_OPERATOR_4G:
            type = "4G";
            break;
          case NetworkTypeUtil.TYPE_OPERATOR_5G:
            type = "5G";
            break;
          case NetworkTypeUtil.TYPE_UNKNOWN:
            type = "未知";
            break;
        }
        sb.append("\n").append(type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          requestPermissions(new String[] { Manifest.permission.ACCESS_WIFI_STATE }, 111);
        }
        sb.append("\nMAC:").append(DeviceUtil.getMacAddress(this));
        sb.append("\nIP:").append(NetworkTypeUtil.getIp(this));

        sb.append("\n imei :").append(DeviceUtil.getIMEI(this));
        sb.append("\n imsi :").append(DeviceUtil.getIMSI(this));
        sb.append("\n AndroidID :").append(DeviceUtil.getAndroidID(this));
        sb.append("\n root :").append(DeviceUtil.isDeviceRooted());



        sb.append("\n \n \n版本信息:");
        sb.append("\nversionCode:"+ AppUtil.getVersionCode(this));
        sb.append("\nversionName:"+ AppUtil.getAppVersionName(this));
        sb.append("\nappName:"+ AppUtil.getAppName(this));
        textView.setText(sb.toString());
        break;
      case R.id.setting_btn:
        startActivity(AppUtil.getAppDetailsSettingsIntent(getPackageName()));
        break;
      case R.id.home_btn:
        startActivity(AppUtil.getHomeIntent());
        break;
    }
  }
}
