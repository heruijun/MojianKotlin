package com.it51buy.mojian.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.it51buy.mojian.R;

public class MenuActivity extends Activity {
    FrameLayout fLayoutMenuMultikey;
    FrameLayout fLayoutMenuMessageCenter;
    FrameLayout fLayoutMenuAppDownload;
    FrameLayout fLayoutMenuSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.RIGHT;
        WindowManager wm = (WindowManager) MenuActivity.this.getApplicationContext().getSystemService("window");
        lp.x = 20;
        lp.y = lp.y - wm.getDefaultDisplay().getHeight() / 5;
        getWindow().setAttributes(lp);

        prepareViews();
    }

    private void prepareViews() {
        fLayoutMenuMultikey = (FrameLayout) findViewById(R.id.fLayoutMenuMultikey);
        fLayoutMenuMessageCenter = (FrameLayout) findViewById(R.id.fLayoutMenuMessageCenter);
        fLayoutMenuAppDownload = (FrameLayout) findViewById(R.id.fLayoutAppDownload);
        fLayoutMenuSetting = (FrameLayout) findViewById(R.id.fLayoutSetting);
        fLayoutMenuMultikey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MultikeyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fLayoutMenuMessageCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MessageCenterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fLayoutMenuAppDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AppDownloadActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fLayoutMenuSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
