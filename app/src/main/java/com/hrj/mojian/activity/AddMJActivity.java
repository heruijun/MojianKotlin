package com.hrj.mojian.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.hrj.mojian.R;
import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.bean.MJContact;
import com.hrj.mojian.eventcenter.EC;
import com.hrj.mojian.eventcenter.EventName;
import com.hrj.mojian.exception.NoSdCardException;
import com.hrj.mojian.utils.Define;
import com.hrj.mojian.utils.FileUtil;
import com.hrj.mojian.utils.MJLog;
import com.hrj.mojian.utils.MJToast;
import com.hrj.mojian.view.AddAdvancedView;
import com.hrj.mojian.view.AddApplicationView;
import com.hrj.mojian.view.AddSystemView;
import com.hrj.mojian.view.BaseTabView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddMJActivity extends Activity implements OnClickListener {

	private static final String TAG = AddMJActivity.class.getSimpleName();

	private BaseTabView mBaseTabView;
	private MJBean mjBean = null;
	private View mBack;
	private Paint mPhotoPaint;
	private Paint mTextPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_mj);
		Bundle bundle = getIntent().getExtras();
		if (null != bundle && bundle.containsKey(Define.BUNDLE_KEY_REPLACE_MJITEM)) {
			int hashCode = bundle.getInt(Define.BUNDLE_KEY_REPLACE_MJITEM);
			MJBean[] mjBeans = new MJBean[] { null };
			EC.getInstence().dispatch(mjBeans, EventName.find_function_item_by_hashcode, hashCode);
			mjBean = mjBeans[0];
		}
		initView();
	}

	private void initView() {
		mBack = findViewById(R.id.back);
		mBack.setOnClickListener(this);

		mBaseTabView = new BaseTabView(findViewById(R.id.root));
		mBaseTabView.addTabItem(new AddApplicationView(this), new AddSystemView(this), new AddAdvancedView(this));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		back();
	}

	public MJBean getMJItem() {
		return mjBean;
	}

	public void back() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			back();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case Define.REQUEST_BY_PHONE:
			handlerResult(data, R.drawable.default_head, Define.REQUEST_BY_PHONE, Intent.ACTION_CALL);
			break;

		case Define.REQUEST_BY_SMS:
			handlerResult(data, R.drawable.default_head, Define.REQUEST_BY_SMS, Intent.ACTION_SENDTO);
			break;

		default:
			break;
		}
	}

	/**
	 * Returns an Intent describing a direct text message shortcut
	 * 
	 * @param data
	 *            The data from the phone number picker
	 * @param actionResId
	 * @param action
	 * @return An Intent describing a phone number shortcut
	 */
	private void handlerResult(Intent data, int actionResId, int type, String action) {
		Uri phoneUri = data.getData();
		long personId = 0;
		String name = null;
		String number = null;
		Cursor cursor = getContentResolver().query(phoneUri, new String[] { Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER }, null, null, null);
		try {
			cursor.moveToFirst();
			personId = cursor.getInt(0);
			name = cursor.getString(1);
			number = cursor.getString(2);
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, personId);
		Bitmap bitmap = generatePhoneNumberIcon(personUri, type, actionResId);
		String fileName = "";
		try {
			fileName = FileUtil.getContactFile();
			File file = new File(fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (NoSdCardException e) {
			e.printStackTrace();
			MJToast.show(R.string.no_sdcard, Toast.LENGTH_SHORT);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bitmap.recycle();
		}

		MJBean newMjBean = new MJBean(MJBean.CONTACT_TYPE, new MJContact(name, number, fileName, type));

		if (null == mjBean) {
			EC.getInstence().dispatch(EventName.add_function_item_to_home, newMjBean);
		} else {
			EC.getInstence().dispatch(EventName.replace_function_item_from_home, mjBean, newMjBean);
		}

		back();
	}

	/**
	 * Generates a phone number shortcut icon.Adds an overlay describing the
	 * type of the phone number , add if there is a photo also adds the call
	 * action icon
	 * 
	 * @param personUri
	 *            The person the phone number belongs to
	 * @param type
	 *            The type of the phone number
	 * @param actionResId
	 *            The ID fot the action resource
	 * @return The bitmap for the icon
	 */
	private Bitmap generatePhoneNumberIcon(Uri personUri, int type, int actionResId) {
		final Resources resources = getResources();
		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), personUri);
		Bitmap photo = BitmapFactory.decodeStream(input);
		if (null == photo) {
			// If there is not a photo use the generic phone action icon instead
			photo = getPhoneActionIcon(resources, actionResId);
			if (null != photo) {
				MJLog.d(TAG, "photo width: " + photo.getWidth() + "   height: " + photo.getHeight());
			} else {
				return null;
			}
		}

		// Set up the drawing class
		int iconSize = resources.getDimensionPixelOffset(R.dimen.default_photo_size);
		Bitmap icon = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
		Canvas canvas = new Canvas(icon);
		// Copy in the photo
		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());
		Rect dst = new Rect(0, 0, iconSize, iconSize);
		canvas.drawBitmap(photo, src, dst, getPhotoPaint());

		// Create an overlay for the phone number type
		String overlay = null;
		switch (type) {
		case Define.REQUEST_BY_PHONE:
			overlay = "T";
			break;

		case Define.REQUEST_BY_SMS:
			overlay = "S";
			break;
		}
		if (overlay != null) {
			canvas.drawText(overlay, 2, 16, getTextPaint());
		}

		photo.recycle();

		return icon;
	}

	private Paint getPhotoPaint() {
		if (null == mPhotoPaint) {
			mPhotoPaint = new Paint();
			mPhotoPaint.setDither(true);
			mPhotoPaint.setFilterBitmap(true);
		}
		return mPhotoPaint;
	}

	private Paint getTextPaint() {
		if (null == mTextPaint) {
			mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
			mTextPaint.setTextSize(20.0f);
			mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
			mTextPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
		}
		return mTextPaint;
	}

	/**
	 * Return the phone action icon
	 * 
	 * @param resources
	 *            The resource to load the icon from
	 * @param resId
	 *            The resource id to load
	 * @return The icon for the phone action icon
	 */
	private Bitmap getPhoneActionIcon(Resources resources, int resId) {
		return BitmapFactory.decodeResource(getResources(), resId);
	}

}
