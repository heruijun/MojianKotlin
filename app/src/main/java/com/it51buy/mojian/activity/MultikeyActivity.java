package com.it51buy.mojian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.it51buy.mojian.R;

public class MultikeyActivity extends Activity{
	private ImageView imgMultikeyArrow;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multikey);
        prepareView();
	}
	
	private void prepareView(){
		imgMultikeyArrow = (ImageView)findViewById(R.id.imgMultiKeyArrow);
		
		imgMultikeyArrow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
	}
}
