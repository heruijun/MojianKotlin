package com.hrj.mojian.bean;

import java.io.Serializable;

public class MJItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private MJBean mjBean;
	private boolean enable;

	public MJItem(MJBean mjBean, boolean enable) {
		super();
		this.mjBean = mjBean;
		this.enable = enable;
	}

	public MJBean getMjBean() {
		return mjBean;
	}

	public void setMjBean(MJBean mjBean) {
		this.mjBean = mjBean;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
