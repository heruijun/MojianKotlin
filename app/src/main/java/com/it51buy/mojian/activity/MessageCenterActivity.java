package com.it51buy.mojian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.it51buy.mojian.R;

public class MessageCenterActivity extends Activity{
	private ImageView imgMessageArrow;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center);
        prepareView();
	}
	
	private void prepareView(){
		imgMessageArrow = (ImageView)findViewById(R.id.imgMessageCenterArrow);
		
		imgMessageArrow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
	}
}
