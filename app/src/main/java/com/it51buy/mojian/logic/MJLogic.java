package com.it51buy.mojian.logic;

import com.it51buy.mojian.bean.MJContact;

public interface MJLogic {

	void capture();

	void recordSound();

	void openCamera();

	void openFlashLight();

	void screenShot();

	void recentApp();

	void clearMemory();

	void openApp(String packageName);

	void setMobileNet();

	void setHotSpot();

	void setWifi();

	void setBluetooth();

	void setDataSynchronization();

	void setFlyMode();

	void setNFC();

	void setBrightness();

	void setAutoRotation();

	void setFullScreenMode();

	void setFlashLight();

	void setGPS();

	void setMute();

	void setHapticFeedback();

	void setVibrateRinging();

	void callPhone(MJContact contact);

	void sendSMS(MJContact contact);
}
