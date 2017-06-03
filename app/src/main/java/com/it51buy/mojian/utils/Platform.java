package com.it51buy.mojian.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.it51buy.mojian.App;

public class Platform {

	private static String getResourcePackageName() {
		return getContext().getPackageName();
	}
	
	private static Context getContext(){
		return App.getInstance();
	}
	
	public static Resources getResources(){
		return getContext().getResources();
	}

	public static String getString(int id) {
		return getResources().getString(id);
	}

	public static String getString(String name) {
		int resString = getResources().getIdentifier(name, "string", getResourcePackageName());
		return getResources().getString(resString);
	}

	public static int getStringId(String name) {
		return getResources().getIdentifier(name, "string", getResourcePackageName());
	}

	public static String[] getStringArray(String name) {
		int resString = getResources().getIdentifier(name, "array", getResourcePackageName());
		return getResources().getStringArray(resString);
	}

	public static int[] getIntArray(String name) {
		int resString = getResources().getIdentifier(name, "array", getResourcePackageName());
		return getResources().getIntArray(resString);
	}

	public static int getLayoutId(String name) {
		int resID = getResources().getIdentifier(name, "layout", getResourcePackageName());
		return resID;
	}
	
	public static Drawable getDrawable(String name){
		return getResources().getDrawable(getDrawableId(name));
	}

	public static int getDrawableId(String name) {
		int resID = getResources().getIdentifier(name, "drawable", getResourcePackageName());
		return resID;
	}

	public static int getAnimId(String name) {
		int resID = getResources().getIdentifier(name, "anim", getResourcePackageName());
		return resID;
	}

	public static int getArrayId(String name) {
		int resID = getResources().getIdentifier(name, "array", getResourcePackageName());
		return resID;
	}

	public static int getAttrId(String name) {
		int resID = getResources().getIdentifier(name, "attr", getResourcePackageName());
		return resID;
	}

	public static int getId(String name) {
		int resID = getResources().getIdentifier(name, "id", getResourcePackageName());
		return resID;
	}

	public static int getStyleId(String name) {
		int resID = getResources().getIdentifier(name, "style", getResourcePackageName());
		return resID;
	}

	public static int getDimenId(String name) {
		int resID = getResources().getIdentifier(name, "dimen", getResourcePackageName());
		return resID;
	}

	public static int getXmlId(String name) {
		int resID = getResources().getIdentifier(name, "xml", getResourcePackageName());
		return resID;
	}

	public static int getColorId(String name) {
		int resID = getResources().getIdentifier(name, "color", getResourcePackageName());
		return resID;
	}

	public static int getColor(int id) {
		return getResources().getColor(id);
	}

	public static int getDimen(int id) {
		return getResources().getDimensionPixelSize(id);
	}

}
