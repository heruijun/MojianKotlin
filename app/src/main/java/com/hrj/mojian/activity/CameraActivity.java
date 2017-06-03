package com.hrj.mojian.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.hrj.mojian.R;
import com.hrj.mojian.exception.NoSdCardException;
import com.hrj.mojian.utils.FileUtil;
import com.hrj.mojian.utils.MJToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {

	private SurfaceView mySurfaceView;
	private SurfaceHolder myHolder;
	private Camera mCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		initSurface();
	}

	@SuppressWarnings("deprecation")
	private void initSurface() {
		mySurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
		myHolder = mySurfaceView.getHolder();
		myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			initCamera();
		}
	}

	private void initCamera() {
		try {
			checkCameraHardware();
			openCamera();
			autoFocus();
		} catch (Exception e) {
			MJToast.show(R.string.no_camera, Toast.LENGTH_SHORT);
			stopCamera();
		}
	}

	private void checkCameraHardware() throws Exception {
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			throw new Exception();
		}
	}

	private void openCamera() throws Exception {
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(myHolder);
			initCameraParam();
			mCamera.startPreview();
		} catch (Exception e) {
			throw new Exception();
		}
	}

	private void initCameraParam() {
		Parameters parameters = mCamera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		parameters.setJpegQuality(100);
		parameters.setPictureFormat(ImageFormat.JPEG);
	}

	private void stopCamera() {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		finish();
	}

	private void autoFocus() {
		mCamera.autoFocus(myAutoFocus);
	}

	private AutoFocusCallback myAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			mCamera.takePicture(null, null, myPicCallback);
		}
	};

	private PictureCallback myPicCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				File f = FileUtil.getPhotoFile();
				String path = f.getAbsolutePath();
				FileOutputStream fOut = new FileOutputStream(f);
				fOut.write(data);
				fOut.flush();
				fOut.close();
				FileUtil.galleryAddPic(path, CameraActivity.this);
				MJToast.show(String.format(getString(R.string.take_photo_success), path), Toast.LENGTH_LONG);
			} catch (NoSdCardException e) {
				MJToast.show(R.string.no_sdcard, Toast.LENGTH_SHORT);
			} catch (IOException e) {
				MJToast.show(R.string.no_sdcard, Toast.LENGTH_SHORT);
			} finally {
				stopCamera();
			}
		}

	};

}
