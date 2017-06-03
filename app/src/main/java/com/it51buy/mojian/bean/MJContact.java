package com.it51buy.mojian.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.it51buy.mojian.utils.Define;
import com.it51buy.mojian.utils.Platform;

import java.io.Serializable;

public class MJContact implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String number;
	private String fileName;
	private int type;

	public MJContact(String name, String number, String fileName, int type) {
		super();
		this.name = name;
		this.number = number;
		this.fileName = fileName;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public Bitmap getBitmap() {
		Bitmap bitmap = BitmapFactory.decodeFile(fileName);
		if (null == bitmap) {
			if (Define.REQUEST_BY_PHONE == type) {
				bitmap = BitmapFactory.decodeResource(Platform.getResources(), Platform.getDrawableId("icon_ring"));
			} else {
				bitmap = BitmapFactory.decodeResource(Platform.getResources(), Platform.getDrawableId("icon_rotation_land"));
			}
		}
		return bitmap;
	}

}
