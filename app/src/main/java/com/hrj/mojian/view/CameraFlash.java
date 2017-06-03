/* 
 * Copyright (C) 2008-2012 OpenIntents.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hrj.mojian.view;

import com.hrj.mojian.R;
import com.hrj.mojian.utils.MJToast;

import android.hardware.Camera;
import android.widget.Toast;

public class CameraFlash {

	public boolean checkAvailable() {
		try {
			Class.forName("android.hardware.Camera");
			return true;
		} catch (ClassNotFoundException e) {
			MJToast.show(R.string.no_flash_light, Toast.LENGTH_SHORT);
			return false;
		}
	}

	private Camera mCamera;

	public boolean isOff() {
		return mCamera == null;
	}

	public void lightsOn(){
		if (mCamera == null){
			mCamera = Camera.open();
			Camera.Parameters params = mCamera.getParameters();
			params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(params);
		}
	}

	public void lightsOff(){
		if (mCamera != null){
			Camera.Parameters params = mCamera.getParameters();
			params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(params);
			mCamera.release();
			mCamera = null;
		}
	}

}
