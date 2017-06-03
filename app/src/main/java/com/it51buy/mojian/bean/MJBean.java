package com.it51buy.mojian.bean;

import com.it51buy.mojian.R;
import com.it51buy.mojian.logic.MJLogic;
import com.it51buy.mojian.logic.MJLogicFactory;
import com.it51buy.mojian.utils.Define;
import com.it51buy.mojian.utils.Platform;

import java.io.Serializable;

public class MJBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SYSTEM_TYPE = 0;// 系统类型功能
    public static final int APP_TYPE = 1;// app类型功能
    public static final int BASE_TYPE = 2;// 默认功能
    public static final int CONTACT_TYPE = 3;// 联系人类型

    private int type;
    private String name;
    private String image;
    private String appName;
    private String packageName;
    private boolean isUsable = true;// 该应用在魔键是否可用

    private MJContact contact;

    public MJBean() {
    }

    public MJBean(int type, MJContact contact) {
        super();
        this.type = type;
        this.contact = contact;
    }

    public MJBean(int type, String name, String image) {
        this.type = type;

        if (type == APP_TYPE) {
            this.appName = name;
            this.packageName = image;
        } else {
            this.name = name;
            this.image = image;
        }
    }

    public MJBean(int type, String name, String image, boolean isUsable) {
        this.type = type;
        this.isUsable = isUsable;

        if (type == APP_TYPE) {
            this.appName = name;
            this.packageName = image;
        } else {
            this.name = name;
            this.image = image;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }

    public MJContact getContact() {
        return contact;
    }

    public void setContact(MJContact contact) {
        this.contact = contact;
    }

    public int getImageId() {
        return null == image ? 0 : Platform.getDrawableId(image);
    }

    public int getNameId() {
        return null == name ? 0 : Platform.getStringId(name);
    }

    public void onClick() {
        if (type == APP_TYPE) {
            handleAppClick();
        } else if (type == CONTACT_TYPE) {
            handleContactClick();
        } else {
            handleClick();
        }
    }

    private void handleAppClick() {
        getMjLogic().openApp(packageName);
    }

    private void handleContactClick() {
        if (Define.REQUEST_BY_PHONE == contact.getType()) {
            getMjLogic().callPhone(contact);
        } else {
            getMjLogic().sendSMS(contact);
        }
    }

    private void handleClick() {
        int resourceId = getNameId();
        switch (resourceId) {
            case R.string.capture:
                getMjLogic().capture();
                break;

            case R.string.sound_recording:
                getMjLogic().recordSound();
                break;

            case R.string.open_camera:
                getMjLogic().openCamera();
                break;

            case R.string.open_flash_light:
                getMjLogic().openFlashLight();
                break;

            case R.string.screenshot:
                getMjLogic().screenShot();
                break;

            case R.string.recently_opened:
                getMjLogic().recentApp();
                break;

            case R.string.clear_memory:
                getMjLogic().clearMemory();
                break;

            case R.string.mobile_net:
                getMjLogic().setMobileNet();
                break;

            case R.string.hot_spot:
                getMjLogic().setHotSpot();
                break;

            case R.string.wifi:
                getMjLogic().setWifi();
                break;

            case R.string.bluetooth:
                getMjLogic().setBluetooth();
                break;

            case R.string.data_synchronization:
                getMjLogic().setDataSynchronization();
                break;

            case R.string.fly_mode:
                getMjLogic().setFlyMode();
                break;

            case R.string.nfc:
                getMjLogic().setNFC();
                break;

            case R.string.brightness:
                getMjLogic().setBrightness();
                break;

            case R.string.auto_rotation:
                getMjLogic().setAutoRotation();
                break;

            case R.string.full_screen_mode:
                getMjLogic().setFullScreenMode();
                break;

            case R.string.flash_light:
                getMjLogic().setFlashLight();
                break;

            case R.string.gps:
                getMjLogic().setGPS();
                break;

            case R.string.mute:
                getMjLogic().setMute();
                break;

            case R.string.vibration_ringing:
                getMjLogic().setVibrateRinging();
                break;

            case R.string.haptic_feedback:
                getMjLogic().setHapticFeedback();
                break;

            default:
                break;
        }
    }

    private MJLogic getMjLogic() {
        return MJLogicFactory.getMJLogic();
    }

}