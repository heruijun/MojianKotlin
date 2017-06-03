package com.hrj.mojian;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.bean.MJItem;
import com.hrj.mojian.service.MJBinder;
import com.hrj.mojian.service.MJService;
import com.hrj.mojian.utils.MJLog;
import com.hrj.mojian.utils.MJToast;

import java.util.LinkedList;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App mInstance;
    private AppPersistenceAPI mAppPersistence;
    private MJService mService;

    public static App getInstance() {
        return mInstance;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mService = ((MJBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        MJLog.d(TAG, "MJApp create");
        mInstance = this;
        initialize();
        bindService();
        initCrashHandler();
    }

    private void initialize() {
        mAppPersistence = new AppPersistenceAPI(this);
        MJToast.init(this);
    }

    public void bindService() {
        Intent intent = new Intent(this, MJService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initCrashHandler() {
        CrashHandler catchExcep = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    public MJItem[] getMJItems() {
        return mAppPersistence.getMJItems();
    }

    public void setMjItems(MJItem[] mjItems) {
        mAppPersistence.setMjItems(mjItems);
        mService.setMjItems(mjItems);
    }

    public void saveMJItems(MJItem[] mjItems) {
        mAppPersistence.setMjItems(mjItems);
    }

    public LinkedList<MJBean> getFunctionItems() {
        return mAppPersistence.getFunctionItems();
    }

    public void setFunctionItems(LinkedList<MJBean> mjItems) {
        mAppPersistence.setFunctionItems(mjItems);
    }

    public void setKeyVibrationToggle(boolean checked) {
        mService.setKeyVibrationToggle(checked);
    }

    public void setTurnoverInvalidToggle(boolean checked) {
        mService.setTurnoverInvalidToggle(checked);
    }

    public void setNotificationToggle(boolean checked) {
        mService.setNotificationToggle(checked);
    }

    public void setImmersiveMode() {
        mService.setFullScreenMode();
    }

}