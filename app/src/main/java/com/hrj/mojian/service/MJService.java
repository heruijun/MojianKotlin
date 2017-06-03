package com.hrj.mojian.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hrj.mojian.App;
import com.hrj.mojian.AppPreference;
import com.hrj.mojian.R;
import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.bean.MJItem;
import com.hrj.mojian.utils.Define;
import com.hrj.mojian.utils.MJLog;
import com.hrj.mojian.utils.MJToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MJService extends Service implements OnTouchListener, View.OnKeyListener {
    private static final String TAG = MJService.class.getSimpleName();

    private static final String ACTION_MJ_1_CLICK = "ACTION_MJ_1_CLICK";
    private static final String ACTION_MJ_2_CLICK = "ACTION_MJ_2_CLICK";
    private static final String ACTION_MJ_3_CLICK = "ACTION_MJ_3_CLICK";
    private static final String ACTION_MJ_4_CLICK = "ACTION_MJ_4_CLICK";

    private static final int NOTIFY_ID = 100001;

    private boolean mKeyVibrationToggle;
    private boolean mTurnoverInvalidToggle;
    private boolean mNotificationToggle;

    private IBinder mBinder = new MJBinder(this);

    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private ImageView mImageView = null;
    private int width;
    private int statusBarHeight;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private boolean showed = false;

    private MJOrientationEventListener mOrientationEventListener;
    private boolean mIsPortrait = true;
    private boolean mIsUp = true;

    private Vibrator mVibrator;

    private ActivityManager mActivityManager;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteView;

    private MJItem[] mjItems;

    private ImageView mImmerModeView;
    private boolean isImmerMode = false;
    private WindowManager.LayoutParams localLayoutParams;

    @Override
    public IBinder onBind(Intent intent) {
        MJLog.d(TAG, TAG + " onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        MJLog.d(TAG, TAG + " onUnbind -------------->");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        MJLog.d(TAG, TAG + " onRebind -------------->");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MJLog.d(TAG, TAG + " onStartCommand -------------->");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MJLog.d(TAG, TAG + " onCreate -------------->");
        mjItems = App.getInstance().getMJItems();
        // 10 ms updates.
        initSensor();
        registerActionReceiver();
        initStatus();
        createView(MJService.this.getApplicationContext());
        updateNotification();
        // createImmerView();
    }

    private void initSensor() {
        mOrientationEventListener = new MJOrientationEventListener(this) {

            @Override
            public void onOrientationChanged(int orientation) {
                if (-1 == orientation) {
                    return;
                }
                if ((orientation > 45 && orientation < 135) || (orientation > 225 && orientation < 315)) {
                    mIsPortrait = false;
                } else {
                    mIsPortrait = true;
                }
            }

            @Override
            public void onWardChanged(boolean ward) {
                mIsUp = ward;
            }
        };
        mOrientationEventListener.enable();
    }

    private void registerActionReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        localIntentFilter.addAction("android.intent.action.SCREEN_ON");
        localIntentFilter.addAction("android.intent.action.USER_PRESENT");
        localIntentFilter.addAction(ACTION_MJ_1_CLICK);
        localIntentFilter.addAction(ACTION_MJ_2_CLICK);
        localIntentFilter.addAction(ACTION_MJ_3_CLICK);
        localIntentFilter.addAction(ACTION_MJ_4_CLICK);
        registerReceiver(mReceiver, localIntentFilter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_MJ_1_CLICK)) {
                updateMJByClick(mjItems[0], R.id.notifi_mj_1_bg);
            } else if (action.equals(ACTION_MJ_2_CLICK)) {
                updateMJByClick(mjItems[1], R.id.notifi_mj_2_bg);
            } else if (action.equals(ACTION_MJ_3_CLICK)) {
                updateMJByClick(mjItems[2], R.id.notifi_mj_3_bg);
            } else if (action.equals(ACTION_MJ_4_CLICK)) {
                updateMJByClick(mjItems[3], R.id.notifi_mj_4_bg);
            }
        }

    };

    private void updateMJByClick(MJItem mjItem, int viewId) {
        if (null == mjItem) {
            return;
        }
        boolean status = !mjItem.isEnable();
        mjItem.setEnable(status);

        App.getInstance().saveMJItems(mjItems);

        getRemote().setViewVisibility(viewId, getVisible(!status));
        updateNotification();
    }

    private int getVisible(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    private void initStatus() {
        mKeyVibrationToggle = AppPreference.getBoolean(Define.PREFERENCES_KEY_VIBRATION, true);
        mTurnoverInvalidToggle = AppPreference.getBoolean(Define.PREFERENCES_TURNOVER_INVALID, true);
        mNotificationToggle = AppPreference.getBoolean(Define.PREFERENCES_NOTIFICATION, true);
    }

    @SuppressWarnings("deprecation")
    private void createView(Context paramContext) {
        mImageView = new ImageView(paramContext);
        mImageView.setBackgroundColor(Color.TRANSPARENT);

        // 获取WindowManager
        wm = (WindowManager) paramContext.getSystemService("window");
        width = wm.getDefaultDisplay().getWidth();
        statusBarHeight = getStatusBarHeight();
        // 设置LayoutParams(全局变量）相关参数
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = 2010;
        wmParams.format = -3;
        wmParams.flags = 1320;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
        wmParams.x = 0;
        wmParams.y = 0;

        if ((width > 0) && (statusBarHeight > 0)) {
            wmParams.height = statusBarHeight;
            wmParams.width = width;
            wm.addView(mImageView, wmParams);
        }
        mImageView.setOnTouchListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int ori = newConfig.orientation;// 获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            mImageView.setVisibility(View.GONE);
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    private void updateNotification() {
        if (mNotificationToggle) {
            getNotificationManager().notify(NOTIFY_ID, getBuilder().build());
        } else {
            getNotificationManager().cancel(NOTIFY_ID);
        }
    }

    private NotificationManager getNotificationManager() {
        if (null == mNotifyManager) {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotifyManager;
    }

    private NotificationCompat.Builder getBuilder() {
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this);
            initMJNotifiImage(getRemote());
            initClickEvent();
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            mBuilder.setContent(getRemote());
            mBuilder.setOngoing(true);
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        } else {
            initMJNotifiImage(getRemote());
            mBuilder.setContent(getRemote());
        }
        return mBuilder;
    }

    private RemoteViews getRemote() {
        if (null == mRemoteView) {
            mRemoteView = new RemoteViews(getPackageName(), R.layout.mj_notification);
        }
        return mRemoteView;
    }

    private void initMJNotifiImage(RemoteViews remoteView) {
        setMJImage(remoteView, R.id.notifi_mj_1_img, mjItems[0]);
        setMJImage(remoteView, R.id.notifi_mj_2_img, mjItems[1]);
        setMJImage(remoteView, R.id.notifi_mj_3_img, mjItems[2]);
        setMJImage(remoteView, R.id.notifi_mj_4_img, mjItems[3]);

        refreshMJBg(remoteView, R.id.notifi_mj_1_bg, mjItems[0]);
        refreshMJBg(remoteView, R.id.notifi_mj_2_bg, mjItems[1]);
        refreshMJBg(remoteView, R.id.notifi_mj_3_bg, mjItems[2]);
        refreshMJBg(remoteView, R.id.notifi_mj_4_bg, mjItems[3]);
    }

    private void setMJImage(RemoteViews remoteView, int viewId, MJItem mjItem) {
        if (null == mjItem) {
            remoteView.setImageViewBitmap(viewId, null);
            return;
        }

        int type = mjItem.getMjBean().getType();
        switch (type) {
            case MJBean.BASE_TYPE:
            case MJBean.SYSTEM_TYPE:
                remoteView.setImageViewResource(viewId, mjItem.getMjBean().getImageId());
                break;

            case MJBean.APP_TYPE:
                try {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getPackageManager().getApplicationIcon(mjItem.getMjBean().getPackageName());
                    remoteView.setImageViewBitmap(viewId, bitmapDrawable.getBitmap());
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                    MJToast.show(R.string.no_app_icon, Toast.LENGTH_SHORT);
                }
                break;

            case MJBean.CONTACT_TYPE:
                remoteView.setImageViewBitmap(viewId, mjItem.getMjBean().getContact().getBitmap());
                break;

            default:
                break;
        }

    }

    private void refreshMJBg(RemoteViews remoteView, int viewId, MJItem mjItem) {
        if (null == mjItem) {
            remoteView.setViewVisibility(viewId, getVisible(false));
        } else {
            remoteView.setViewVisibility(viewId, getVisible(!mjItem.isEnable()));
        }
    }

    private void initClickEvent() {
        getRemote().setOnClickPendingIntent(R.id.notifi_mj_1_img, getClickIntent(ACTION_MJ_1_CLICK));
        getRemote().setOnClickPendingIntent(R.id.notifi_mj_2_img, getClickIntent(ACTION_MJ_2_CLICK));
        getRemote().setOnClickPendingIntent(R.id.notifi_mj_3_img, getClickIntent(ACTION_MJ_3_CLICK));
        getRemote().setOnClickPendingIntent(R.id.notifi_mj_4_img, getClickIntent(ACTION_MJ_4_CLICK));
    }

    private PendingIntent getClickIntent(String action) {
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        return pendingIntent;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        MJLog.d(TAG, TAG + "onDestroy()");
        if ((width > 0) && (statusBarHeight > 0)) {
            wm.removeView(mImageView);
        }
        unregisterReceiver(mReceiver);
        mOrientationEventListener.disable();
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY();
        MJLog.d(TAG, "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                MJLog.d(TAG, "startX" + mTouchStartX + "====startY" + mTouchStartY);
                break;

            case MotionEvent.ACTION_MOVE:
                if ((!showed) && (y > statusBarHeight)) {
                    showNotifications(this.getApplicationContext());
                    showed = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                mTouchStartX = mTouchStartY = 0;
                showed = false;
                onClickMJ();
                break;
        }
        return true;
    }

    private Vibrator getVibrator() {
        if (null == mVibrator) {
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        return mVibrator;
    }

    private void showNotifications(Context context) {
        try {
            Object service = context.getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("expand");
            expand.invoke(service);
        } catch (NoSuchMethodException e) {
            try {
                Object obj = context.getSystemService("statusbar");
                Class.forName("android.app.StatusBarManager").getMethod("expandNotificationsPanel", new Class[0]).invoke(obj, (Object[]) null);
            } catch (Exception e2) {
            }
        } catch (Exception e) {
        }
    }

    private void onClickMJ() {
        if ((x >= 0) && (x <= wmParams.width / 4) && (y >= 0) && (y <= wmParams.height) && (mjItems[0] != null)) {
            execute(mjItems[0]);
            return;
        }
        if ((x > width / 4) && (x <= width / 2) && (y >= 0) && (y <= wmParams.height) && (mjItems[1] != null)) {
            execute(mjItems[1]);
            return;
        }
        if ((x > width / 2) && (x <= (width / 4) * 3) && (y >= 0) && (y <= wmParams.height) && (mjItems[2] != null)) {
            execute(mjItems[2]);
            return;
        }
        if ((x > (width / 4) * 3) && (x <= width) && (y >= 0) && (y <= wmParams.height) && (mjItems[3] != null)) {
            execute(mjItems[3]);
            return;
        }
    }

    private void execute(MJItem mjItem) {
        // 判断魔键是否屏蔽
        if (!mjItem.isEnable()) {
            return;
        }

        // 判断方向是否正确
        boolean directionRight = mIsPortrait && mIsUp;
        MJLog.d(TAG, "mIsPortrait: " + mIsPortrait + "   mIsUp: " + mIsUp);
        if (mTurnoverInvalidToggle && !directionRight) {
            return;
        }

        // 判断当前进程是否屏蔽魔键
        String curPackageName = getCurPackageName();
        if (!AppPreference.getBoolean(curPackageName, true)) {
            return;
        }

        vibrate();

        mjItem.getMjBean().onClick();
    }

    private void vibrate() {
        if (!mKeyVibrationToggle) {
            return;
        }
        getVibrator().vibrate(100);
    }

    private String getCurPackageName() {
        return getActivityManager().getRunningTasks(1).get(0).topActivity.getPackageName();
    }

    private ActivityManager getActivityManager() {
        if (null == mActivityManager) {
            mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    private ImageView getImmerModeView() {
        if (null == mImmerModeView) {
            mImmerModeView = new ImageView(this);
            mImmerModeView.setBackgroundColor(Color.TRANSPARENT);
            mImmerModeView.setOnKeyListener(this);
        }
        return mImmerModeView;
    }

    private WindowManager.LayoutParams getParams() {
        if (null == localLayoutParams) {
            localLayoutParams = new WindowManager.LayoutParams();
            localLayoutParams.width = 0;
            localLayoutParams.height = 0;
            localLayoutParams.type = 2010;
            localLayoutParams.flags = 8658992;
            localLayoutParams.alpha = -3.0F;
            localLayoutParams.gravity = 53;
        }
        return localLayoutParams;
    }

    public void setFullScreenMode() {
        if (isImmerMode) {
            mImmerModeRun.run();
            wm.removeView(getImmerModeView());
        } else {
            wm.addView(getImmerModeView(), getParams());
            mImmerModeRun.run();
        }
        isImmerMode = !isImmerMode;
    }

    private Runnable mImmerModeRun = new Runnable() {

        @Override
        public void run() {
            int uiOptions = getImmerModeView().getSystemUiVisibility();
            int newUiOptions = uiOptions;
            boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
            MJLog.d(TAG, "isImmersiveModeEnabled : " + isImmersiveModeEnabled);

            if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            if (Build.VERSION.SDK_INT >= 18) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            getImmerModeView().setSystemUiVisibility(newUiOptions);
        }
    };

    public void setMjItems(MJItem[] mjItems) {
        this.mjItems = mjItems;
        updateNotification();
    }

    public void setKeyVibrationToggle(boolean checked) {
        mKeyVibrationToggle = checked;
    }

    public void setTurnoverInvalidToggle(boolean checked) {
        mTurnoverInvalidToggle = checked;
    }

    public void setNotificationToggle(boolean checked) {
        mNotificationToggle = checked;
        updateNotification();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // sendKey(KeyEvent.KEYCODE_BACK);
        if (isImmerMode) {
            setFullScreenMode();
        }
        return true;
    }

    // private void sendKey(final int keyCode) {
    // long now = SystemClock.uptimeMillis();
    // try {
    // KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0);
    // KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0);
    // getIWindowManager().injectKeyEvent(down, false);
    // getIWindowManager().injectKeyEvent(up, false);
    // } catch (RemoteException e) {
    // e.printStackTrace();
    // }
    // }

    // private IWindowManager mIWindowManager;
    //
    // private IWindowManager getIWindowManager() {
    // if (null == mIWindowManager) {
    // mIWindowManager =
    // IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
    // }
    // return mIWindowManager;
    // }

}