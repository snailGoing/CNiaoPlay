package com.chhd.per_library.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.chhd.per_library.util.device.GetEquipInfo;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Andy on 16/9/23.
 */

public class AppUtil {

    private AppUtil() {
    }

    private static String getIMEIId() {
        String IMEIId = "";
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) UiUtil.getContext().getSystemService(TELEPHONY_SERVICE);
            IMEIId = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMEIId;
    }

    private static String getPesudoUniqueID() {

        String deviceId = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        return deviceId;
    }

    private static String getAndroidID() {
        String androidId = Settings.Secure.getString(UiUtil.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    private static String getWLANMACAddress() {
        WifiManager wm = (WifiManager) UiUtil.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String macAddress = wm.getConnectionInfo().getMacAddress();
        return macAddress;
    }

    private static String getBTMACAddress() {
        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String address = "";
        if (m_BluetoothAdapter != null)
            address = m_BluetoothAdapter.getAddress();
        return address;
    }

    /**
     * 获取设备唯一ID
     *
     * @return
     */
    public static String getUniqueID() {
        String id =
                getIMEIId() +
                        getPesudoUniqueID() + getAndroidID() + getWLANMACAddress() + getBTMACAddress();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(id.getBytes(), 0, id.length());
        byte digest[] = messageDigest.digest();
        String uniqueID = new String();
        for (int i = 0; i < digest.length; i++) {
            int b = (0xFF & digest[i]);
            if (b <= 0xF)
                uniqueID += "0";
            uniqueID += Integer.toHexString(b);
        }
        uniqueID = uniqueID.toUpperCase();
        return uniqueID;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public static String getDeviceName() {
        try {
            String[] versions = GetEquipInfo.getVersion();
            return versions[4] + " " + versions[2];
        } catch (Exception e) {
            return "未知设备";
        }
    }

    /**
     * 是否安装某个App
     *
     * @param packageName App的包名
     * @return
     */
    public static boolean isInstallApp(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 获取App版本名称
     *
     * @return
     */
    public static String getVersionName() {
        try {
            PackageManager manager = UiUtil.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(UiUtil.getContext().getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    /**
     * 获取手机号码
     *
     * @return 手机号码
     */
    public static String getMobliePhone() {
        String tel = "";
        try {
            TelephonyManager tm = (TelephonyManager) UiUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            tel = tm.getLine1Number();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return tel;
    }
}
