package com.it51buy.mojian.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import com.it51buy.mojian.R;
import com.it51buy.mojian.adapter.ListViewAddMJAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectAppActivity extends Activity {
    private List<Map<String, Object>> data;
    Map<String, Object> item;
    private ListView listView = null;
    private ListViewAddMJAdapter listViewAdapter;
    private ImageView imgViewArrow;

    private FrameLayout tabApplication;
    private FrameLayout tabSystem;
    private FrameLayout tabAdvanced;

    private ListView listViewSystem;
    private ListView listViewAdvanced;

    private ImageView imgSelAppTabAppFgx;
    private ImageView imgSelAppTabSystemFgx;
    private ImageView imgSelAppTabAdvancedFgx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectapp);

        listView = (ListView) findViewById(R.id.listViewSelApp);
        listViewSystem = (ListView) findViewById(R.id.listViewSystem);
        listViewAdvanced = (ListView) findViewById(R.id.listViewAdvanced);

        imgViewArrow = (ImageView) findViewById(R.id.imgViewArrow);

        tabApplication = (FrameLayout) findViewById(R.id.tabApplication);
        tabSystem = (FrameLayout) findViewById(R.id.tabSystem);
        tabAdvanced = (FrameLayout) findViewById(R.id.tabAdvanced);

        imgSelAppTabAppFgx = (ImageView) findViewById(R.id.imgSelAppTabAppFgx);
        imgSelAppTabSystemFgx = (ImageView) findViewById(R.id.imgSelAppTabSystemFgx);
        imgSelAppTabAdvancedFgx = (ImageView) findViewById(R.id.imgSelAppTabAdvancedFgx);

        data = new ArrayList<Map<String, Object>>();
        listPackages();

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.listview_add_mj_item, new String[]{"icon", "appname", "pname"}, new int[]{
                R.id.image, R.id.text,});

        listView.setAdapter(adapter);

        adapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else return false;
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {    // TODO Auto-generated method stub

                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(pos);
                Intent result = new Intent(null, Uri.parse((String) item.get("pname")));//提示一下，这个是隐式启动
                setResult(RESULT_OK, result);

                finish();

            }
        });

        imgViewArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabApplication.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSelAppTabSystemFgx.setImageResource(R.drawable.appdownfengexian);
                imgSelAppTabAdvancedFgx.setImageResource(R.drawable.appdownfengexian);
                imgSelAppTabAppFgx.setImageResource(R.drawable.fengexian_sel);

                listViewSystem.setVisibility(View.GONE);
                listViewAdvanced.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        tabSystem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSelAppTabAppFgx.setImageResource(R.drawable.appdownfengexian);
                imgSelAppTabAdvancedFgx.setImageResource(R.drawable.appdownfengexian);
                imgSelAppTabSystemFgx.setImageResource(R.drawable.fengexian_sel);

                listView.setVisibility(View.GONE);
                listViewAdvanced.setVisibility(View.GONE);
                listViewSystem.setVisibility(View.VISIBLE);
            }
        });

        tabAdvanced.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSelAppTabAppFgx.setImageResource(R.drawable.appdownfengexian);
                imgSelAppTabSystemFgx.setImageResource(R.drawable.appdownfengexian);
                imgSelAppTabAdvancedFgx.setImageResource(R.drawable.fengexian_sel);

                listView.setVisibility(View.GONE);
                listViewSystem.setVisibility(View.GONE);
                listViewAdvanced.setVisibility(View.VISIBLE);
            }
        });
    }

    class PInfo {
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode = 0;
        private Drawable icon;

        private void prettyPrint() {
            Log.i("taskmanger", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode + "\t");
        }
    }

    private void listPackages() {
        ArrayList<PInfo> apps = getInstalledApps(); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
            item = new HashMap<String, Object>();
            item.put("icon", apps.get(i).icon);
            item.put("appname", apps.get(i).appname);
            item.put("pname", apps.get(i).pname);
            data.add(item);
        }
    }

    private ArrayList<PInfo> getInstalledApps() {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            if (((p.applicationInfo.flags & p.applicationInfo.FLAG_SYSTEM) <= 0)
                    || ((p.applicationInfo.flags & p.applicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)) {

                PInfo newInfo = new PInfo();
                newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
                newInfo.pname = p.packageName;
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                newInfo.icon = p.applicationInfo.loadIcon(getPackageManager()).getCurrent();
                res.add(newInfo);
            }
        }

        return res;
    }
}