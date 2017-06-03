package com.hrj.mojian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.hrj.mojian.R;

public class AppDownloadActivity extends Activity {
	
	private ImageView imgViewArrow;
	private FrameLayout fLayoutTabSelection;
	private FrameLayout fLayoutTabMust;
	private FrameLayout fLayoutTabRankingList;
	private ListView listViewAppSelection;
	private ListView listViewAppMust;
	private ListView listViewAppRankinglist;
	private ImageView imgAppDownTabSelFgx;
	private ImageView imgAppDownTabMustFgx;
	private ImageView imgAppDownTabRankinglistFgx;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_download);
        prepareView();
	}
	
	private void prepareView(){
		
		imgViewArrow = (ImageView)findViewById(R.id.imgAppDownViewArrow);
		
		fLayoutTabSelection = (FrameLayout)findViewById(R.id.tabSelection);
		fLayoutTabMust = (FrameLayout)findViewById(R.id.tabMust);
		fLayoutTabRankingList = (FrameLayout)findViewById(R.id.tabRankingList);
		
		listViewAppSelection = (ListView)findViewById(R.id.listViewAppSelection);
		listViewAppMust = (ListView)findViewById(R.id.listViewAppMust);
		listViewAppRankinglist = (ListView)findViewById(R.id.listViewAppRankinglist);
		
		imgAppDownTabSelFgx = (ImageView)findViewById(R.id.imgAppDownTabSelFgx);
		imgAppDownTabMustFgx = (ImageView)findViewById(R.id.imgAppDownTabMustFgx);
		imgAppDownTabRankinglistFgx = (ImageView)findViewById(R.id.imgAppDownTabRankingListFgx);
		
		imgViewArrow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		fLayoutTabSelection.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				imgAppDownTabMustFgx.setImageResource(R.drawable.appdownfengexian);
				imgAppDownTabRankinglistFgx.setImageResource(R.drawable.appdownfengexian);
				imgAppDownTabSelFgx.setImageResource(R.drawable.fengexian_sel);
				
				listViewAppMust.setVisibility(View.GONE);
				listViewAppRankinglist.setVisibility(View.GONE);
				listViewAppSelection.setVisibility(View.VISIBLE);
				
			}
		});
		
		fLayoutTabMust.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				imgAppDownTabRankinglistFgx.setImageResource(R.drawable.appdownfengexian);
				imgAppDownTabSelFgx.setImageResource(R.drawable.appdownfengexian);
				imgAppDownTabMustFgx.setImageResource(R.drawable.fengexian_sel);
				
				listViewAppRankinglist.setVisibility(View.GONE);
				listViewAppSelection.setVisibility(View.GONE);
				listViewAppMust.setVisibility(View.VISIBLE);
				
			}
		});
		
		fLayoutTabRankingList.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				imgAppDownTabSelFgx.setImageResource(R.drawable.appdownfengexian);
				imgAppDownTabMustFgx.setImageResource(R.drawable.appdownfengexian);
				imgAppDownTabRankinglistFgx.setImageResource(R.drawable.fengexian_sel);
				
				listViewAppSelection.setVisibility(View.GONE);
				listViewAppMust.setVisibility(View.GONE);
				listViewAppRankinglist.setVisibility(View.VISIBLE);
				
			}
		});
	}
}
