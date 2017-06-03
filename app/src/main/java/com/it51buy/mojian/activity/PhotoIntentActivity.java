package com.it51buy.mojian.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.it51buy.mojian.R;
import com.it51buy.mojian.exception.NoSdCardException;
import com.it51buy.mojian.utils.FileUtil;
import com.it51buy.mojian.utils.MJToast;

import java.io.File;

public class PhotoIntentActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO_B = 1;

	private String mCurrentPhotoPath;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			File f = FileUtil.getPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			Uri photoUri = Uri.fromFile(f);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, ACTION_TAKE_PHOTO_B);
		} catch (NoSdCardException e) {
			MJToast.show(R.string.no_sdcard, Toast.LENGTH_SHORT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B:
			if (resultCode == RESULT_OK) {
				MJToast.show(String.format(getString(R.string.take_photo_success), mCurrentPhotoPath), Toast.LENGTH_LONG);
				handleBigCameraPhoto();
			}
			break;
		}
		finish();
	}

	private void handleBigCameraPhoto() {
		if (mCurrentPhotoPath != null) {
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

}