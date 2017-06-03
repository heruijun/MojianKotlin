package com.hrj.mojian;

import android.content.Context;

import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.bean.MJItem;
import com.hrj.mojian.utils.Define;

import java.util.LinkedList;

public class AppPersistenceAPI {

    private AppPersistence mAppPersistence;

    public AppPersistenceAPI(Context context) {
        mAppPersistence = new AppPersistence(context);
    }

    public MJItem[] getMJItems() {
        Object object = mAppPersistence.get(Define.MJ_KEY);
        if (null == object) {
            return new MJItem[4];
        }
        return (MJItem[]) object;
    }

    public void setMjItems(MJItem[] mjItems) {
        mAppPersistence.set(Define.MJ_KEY, mjItems);
        mAppPersistence.saveSettings();
    }

    @SuppressWarnings("unchecked")
    public LinkedList<MJBean> getFunctionItems() {
        Object object = mAppPersistence.get(Define.FUNCTION_KEY);
        if (null == object) {
            return new LinkedList<MJBean>();
        }
        return (LinkedList<MJBean>) object;
    }

    public void setFunctionItems(LinkedList<MJBean> mjItems) {
        mAppPersistence.set(Define.FUNCTION_KEY, mjItems);
        mAppPersistence.saveSettings();
    }

    /**
     * 清除所有持久化数据方法，慎用
     */
    public void clearPersist() {
        mAppPersistence.clearPersist();
    }

}