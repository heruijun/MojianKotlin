package com.hrj.mojian.logic;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.hrj.mojian.App;
import com.hrj.mojian.R;
import com.hrj.mojian.activity.CameraActivity;
import com.hrj.mojian.activity.PhotoIntentActivity;
import com.hrj.mojian.bean.MJContact;
import com.hrj.mojian.exception.NoSdCardException;
import com.hrj.mojian.utils.AppUtil;
import com.hrj.mojian.utils.FileUtil;
import com.hrj.mojian.utils.MJLog;
import com.hrj.mojian.utils.MJToast;
import com.hrj.mojian.utils.Utils;
import com.hrj.mojian.view.CameraFlash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MJLogicImpl implements MJLogic {

    private static final String TAG = MJLogicImpl.class.getSimpleName();

    private App mContext;
    private CameraFlash mCameraFlash;

    public MJLogicImpl() {
        mContext = App.getInstance();
    }

    @Override
    public void capture() {
        Intent intent = new Intent(mContext, CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void recordSound() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("audio/*");
        mContext.startActivity(intent);
    }

    @Override
    public void openCamera() {
        Intent intent = new Intent(mContext, PhotoIntentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void openFlashLight() {
        if (!getCameraFlash().checkAvailable()) {
            return;
        }
        if (getCameraFlash().isOff()) {
            getCameraFlash().lightsOn();
        } else {
            getCameraFlash().lightsOff();
        }
    }

    private CameraFlash getCameraFlash() {
        if (null == mCameraFlash) {
            mCameraFlash = new CameraFlash();
        }
        return mCameraFlash;
    }

    @Override
    public void screenShot() {
        Process sh;
        try {
            File file = FileUtil.getScreenShotFile();
            String filepath = file.getPath();
            sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + filepath).getBytes("ASCII"));
            os.flush();
            os.close();
            sh.waitFor();
            FileOutputStream fos = null;
            Bitmap bitmap = null;
            try {
                fos = mContext.openFileOutput(filepath, Context.MODE_PRIVATE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                InputStream in = mContext.openFileInput(filepath);
                options.inSampleSize = 2;
                options.inPurgeable = true;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != fos) {
                    fos.close();
                }
                if (null != bitmap) {
                    bitmap.recycle();
                }
            }
            Runtime.getRuntime().exec("su", null, null);
            Runtime.getRuntime().exec("rm -rf " + filepath);
            MJToast.show(String.format(mContext.getString(R.string.screen_shot_success), filepath), Toast.LENGTH_LONG);
            FileUtil.galleryAddPic(filepath, mContext);
        } catch (IOException e) {
            e.printStackTrace();
            MJToast.show(R.string.no_root, Toast.LENGTH_SHORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSdCardException e1) {
            e1.printStackTrace();
            MJToast.show(R.string.no_sdcard, Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void recentApp() {
        // Intent intent = new Intent(mContext, RecentAppActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mContext.startActivity(intent);

        try {
            Class serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService", String.class);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
            Class statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());
            Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null, new Object[] { retbinder });
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
        } catch (Exception e) {
            MJToast.show(R.string.no_recent_app, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void clearMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();

        long beforeMem = getAvailMemory(mContext);
        MJLog.d(TAG, "-----------before memory info : " + beforeMem);

        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                RunningAppProcessInfo appProcessInfo = infoList.get(i);
                MJLog.d(TAG, "process name : " + appProcessInfo.processName);
                // importance 该进程的重要程度 分为几个级别，数值越低就越重要。
                MJLog.d(TAG, "importance : " + appProcessInfo.importance);

                if (appProcessInfo.processName.equals("com.hrj.mojian")) {
                    continue;
                }

                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; ++j) {
                        // pkgList 得到该进程下运行的包名
                        MJLog.d(TAG, "It will be killed, package name : " + pkgList[j]);
                        am.killBackgroundProcesses(pkgList[j]);
                    }
                }

            }
        }

        long afterMem = getAvailMemory(mContext);
        MJLog.d(TAG, "----------- after memory info : " + afterMem);

        String prompt = String.format(mContext.getString(R.string.clear_memeory_success), String.valueOf(afterMem - beforeMem));
        MJToast.show(prompt, Toast.LENGTH_LONG);
    }

    // 获取可用内存大小
    private long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        // return Formatter.formatFileSize(context, mi.availMem);//
        // 将获取的内存大小规格化
        MJLog.d(TAG, "可用内存---->>>" + mi.availMem / (1024 * 1024));
        return mi.availMem / (1024 * 1024);
    }

    @Override
    public void openApp(String packageName) {
        AppUtil.openApp(mContext, packageName);
    }

    @Override
    public void setMobileNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        Object[] arg = null;
        try {
            boolean isMobileDataEnable = invokeConnectivityMethod(connectivityManager, "getMobileDataEnabled", arg);
            invokeBooleanArgConnectivityMethod(connectivityManager, "setMobileDataEnabled", !isMobileDataEnable);
        } catch (Exception e) {
            e.printStackTrace();
            MJToast.show(R.string.no_mobile_net, Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean invokeConnectivityMethod(ConnectivityManager connectivityManager, String methodName, Object[] arg) throws Exception {
        Class ownerClass = connectivityManager.getClass();
        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        Boolean isOpen = (Boolean) method.invoke(connectivityManager, arg);
        return isOpen;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object invokeBooleanArgConnectivityMethod(ConnectivityManager connectivityManager, String methodName, boolean value) throws Exception {
        Class ownerClass = connectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(connectivityManager, value);
    }

    @Override
    public void setHotSpot() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        try {
            boolean wifiApState = getWifiApState(wifiManager);
            if (!wifiApState) {
                wifiManager.setWifiEnabled(false);
            }
            // 通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            // 返回热点打开状态
            boolean result = (Boolean) method.invoke(wifiManager, null, !wifiApState);
            if (result) {
            } else {
                MJToast.show(R.string.no_wifi_ap, Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            MJToast.show(R.string.no_wifi_ap, Toast.LENGTH_SHORT);
        }
    }

    private boolean getWifiApState(WifiManager wifiManager) throws Exception {
        final int WIFI_AP_STATE_ENABLING = 12;
        final int WIFI_AP_STATE_ENABLED = 13;

        Method method = wifiManager.getClass().getMethod("getWifiApState");
        int state = (Integer) method.invoke(wifiManager);
        return state == WIFI_AP_STATE_ENABLING || state == WIFI_AP_STATE_ENABLED;
    }

    @Override
    public void setWifi() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiManager.getWifiState();
        boolean enabled = true;
        if (WifiManager.WIFI_STATE_ENABLED == wifiState || WifiManager.WIFI_STATE_ENABLING == wifiState) {
            enabled = false;
        }
        wifiManager.setWifiEnabled(enabled);
    }

    @Override
    public void setBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == bluetoothAdapter) {
            MJToast.show(R.string.no_bluetooth, Toast.LENGTH_SHORT);
            return;
        }
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        } else {
            bluetoothAdapter.enable();
        }
    }

    @Override
    public void setDataSynchronization() {
        boolean flag = ContentResolver.getMasterSyncAutomatically();
        ContentResolver.setMasterSyncAutomatically(!flag);
    }

    @Override
    public void setFlyMode() {
        if (Utils.hasJelly()) {
            int flag = Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AIRPLANE_MODE_ON, 0);
            Settings.System.putInt(mContext.getContentResolver(), android.provider.Settings.Global.AIRPLANE_MODE_ON, flag == 1 ? 0 : 1);
        } else {
            int flag = Settings.System.getInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, flag == 1 ? 0 : 1);
        }
        try {
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNFC() {
        // Turn NFC on/off
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);

        if (nfcAdapter == null) {
            MJToast.show(R.string.no_nfc, Toast.LENGTH_SHORT);
            return;
        }

        try {
            Runtime.getRuntime().exec("su", null, null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        boolean desiredState = !nfcAdapter.isEnabled();

        Class<?> NfcManagerClass;
        Method setNfcEnabled, setNfcDisabled;
        if (desiredState) {
            try {
                NfcManagerClass = Class.forName(nfcAdapter.getClass().getName());
                setNfcEnabled = NfcManagerClass.getDeclaredMethod("enable");
                setNfcEnabled.setAccessible(true);
                setNfcEnabled.invoke(nfcAdapter);
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            try {
                NfcManagerClass = Class.forName(nfcAdapter.getClass().getName());
                setNfcDisabled = NfcManagerClass.getDeclaredMethod("disable");
                setNfcDisabled.setAccessible(true);
                setNfcDisabled.invoke(nfcAdapter);
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }

        if (Build.VERSION.SDK_INT >= 16) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private int mBrightness = 0;

    @Override
    public void setBrightness() {
        final int lowest = 25;

        int screenMode = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

        int brightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
        if (lowest == brightness && 0 != mBrightness) {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, mBrightness);
        } else {
            mBrightness = brightness;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, lowest);
        }
    }

    @Override
    public void setAutoRotation() {
        int flag = Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, flag == 1 ? 0 : 1);
    }

    @Override
    public void setFullScreenMode() {
        mContext.setImmersiveMode();
    }

    @Override
    public void setFlashLight() {
        openFlashLight();
    }

    @Override
    public void setGPS() {
        try {
            Runtime.getRuntime().exec("su", null, null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasPermission = mContext.getPackageManager().checkPermission("android.permission.WRITE_SECURE_SETTINGS", mContext.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        MJLog.d(TAG, "set has permission: " + hasPermission);
        if (hasPermission) {
            try {
                if (gpsEnabled) {
                    Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.GPS_PROVIDER, false);
                } else {
                    Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.GPS_PROVIDER, true);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void setMute() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int mode = audioManager.getRingerMode();
        if (mode == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    @Override
    public void setHapticFeedback() {
        int flag = Settings.System.getInt(mContext.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, 0);
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, flag == 1 ? 0 : 1);
    }

    @Override
    public void setVibrateRinging() {
        int flag = Settings.System.getInt(mContext.getContentResolver(), "vibrate_when_ringing", 0);
        Settings.System.putInt(mContext.getContentResolver(), "vibrate_when_ringing", flag == 1 ? 0 : 1);
    }

    @Override
    public void callPhone(MJContact contact) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + contact.getNumber()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void sendSMS(MJContact contact) {
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, Uri.parse("smsto://" + contact.getNumber()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}