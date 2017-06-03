package com.it51buy.mojian.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.it51buy.mojian.R;
import com.it51buy.mojian.service.MJBinder;
import com.it51buy.mojian.service.MJService;
import com.it51buy.mojian.utils.ImageUtils;

public class MainActivity extends Activity {

    ImageButton imgSetting;
    ImageView imgBtn1, imgBtn2, imgBtn3, imgBtn4;

    Boolean imgBtn1Selected = true;
    Boolean imgBtn2Selected = false;
    Boolean imgBtn3Selected = false;
    Boolean imgBtn4Selected = false;

    LinearLayout lLayoutFunc1, lLayoutFunc2, lLayoutFunc3, lLayoutFunc4, lLayoutFunc5, lLayoutFunc6,
            lLayoutFunc7, lLayoutFunc8, lLayoutFunc9;

    LinearLayout lLayoutHomeUp;

    private MJService mojianService;
    private String appName1 = "";
    private String appName2 = "";
    private String appName3 = "";
    private String appName4 = "";

    Drawable appIcon1, appIcon2, appIcon3, appIcon4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareView();
        bindService();
        getIconByPackageName();
    }

    private void prepareView() {
        imgSetting = (ImageButton) findViewById(R.id.imageViewSetting);

        imgBtn1 = (ImageView) findViewById(R.id.imageViewButton1);
        imgBtn2 = (ImageView) findViewById(R.id.imageViewButton2);
        imgBtn3 = (ImageView) findViewById(R.id.imageViewButton3);
        imgBtn4 = (ImageView) findViewById(R.id.imageViewButton4);
        //9 functions buttons
        lLayoutFunc1 = (LinearLayout) findViewById(R.id.lLayoutFunction1);
        lLayoutFunc2 = (LinearLayout) findViewById(R.id.lLayoutFunction2);
        lLayoutFunc3 = (LinearLayout) findViewById(R.id.lLayoutFuncLine3);
        lLayoutFunc4 = (LinearLayout) findViewById(R.id.lLayoutFunction4);
        lLayoutFunc5 = (LinearLayout) findViewById(R.id.lLayoutFunction5);
        lLayoutFunc6 = (LinearLayout) findViewById(R.id.lLayoutFunction6);
        lLayoutFunc7 = (LinearLayout) findViewById(R.id.lLayoutFunction7);
        lLayoutFunc8 = (LinearLayout) findViewById(R.id.lLayoutFunction8);
        lLayoutFunc9 = (LinearLayout) findViewById(R.id.lLayoutFunction9);

        lLayoutHomeUp = (LinearLayout) findViewById(R.id.lLayoutHomeUp);

        imgBtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Button1", Toast.LENGTH_SHORT).show();
                if (!imgBtn1Selected) {
                    setSelectedBtn(imgBtn1);
                    lLayoutHomeUp.setBackgroundResource(R.drawable.bg_lan);
                    imgBtn1Selected = true;
                    imgBtn2Selected = false;
                    imgBtn3Selected = false;
                    imgBtn4Selected = false;
                }
            }
        });
        imgBtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Button2", Toast.LENGTH_SHORT).show();
                if (!imgBtn2Selected) {
                    setSelectedBtn(imgBtn2);
                    lLayoutHomeUp.setBackgroundResource(R.drawable.bg_ju);
                    imgBtn2Selected = true;
                    imgBtn1Selected = false;
                    imgBtn3Selected = false;
                    imgBtn4Selected = false;
                }
            }
        });
        imgBtn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Button3", Toast.LENGTH_SHORT).show();
                if (!imgBtn3Selected) {
                    setSelectedBtn(imgBtn3);
                    lLayoutHomeUp.setBackgroundResource(R.drawable.bg_hong);
                    imgBtn3Selected = true;
                    imgBtn2Selected = false;
                    imgBtn1Selected = false;
                    imgBtn4Selected = false;
                }
            }
        });
        imgBtn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Button4", Toast.LENGTH_SHORT).show();
                if (!imgBtn4Selected) {
                    setSelectedBtn(imgBtn4);
                    lLayoutHomeUp.setBackgroundResource(R.drawable.bg_lv);
                    imgBtn4Selected = true;
                    imgBtn2Selected = false;
                    imgBtn3Selected = false;
                    imgBtn1Selected = false;
                }
            }
        });

        imgSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        //Add functions. List application.
        lLayoutFunc9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgBtn1Selected) {
                    Intent intent = new Intent(MainActivity.this, SelectAppActivity.class);
                    startActivityForResult(intent, 1);
                }
                if (imgBtn2Selected) {
                    Intent intent2 = new Intent(MainActivity.this, SelectAppActivity.class);
                    startActivityForResult(intent2, 2);
                }
                if (imgBtn3Selected) {
                    Intent intent3 = new Intent(MainActivity.this, SelectAppActivity.class);
                    startActivityForResult(intent3, 3);
                }
                if (imgBtn4Selected) {
                    Intent intent4 = new Intent(MainActivity.this, SelectAppActivity.class);
                    startActivityForResult(intent4, 4);
                }
            }
        });

        //Function 8, to delete setting
        lLayoutFunc8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgBtn1Selected) {
                    appName1 = "";
//					mojianService.setappName("appName1", appName1);
                    setSharedPreference("appName1", appName1);
                    imgBtn1.setImageResource(R.drawable.xiaoyuan);
                }
                if (imgBtn2Selected) {
                    appName2 = "";
//					mojianService.setappName("appName2", appName2);
                    setSharedPreference("appName2", appName2);
                    imgBtn2.setImageResource(R.drawable.xiaoyuan);
                }
                if (imgBtn3Selected) {
                    appName3 = "";
//					mojianService.setappName("appName3", appName3);
                    setSharedPreference("appName3", appName3);
                    imgBtn3.setImageResource(R.drawable.xiaoyuan);
                }
                if (imgBtn4Selected) {
                    appName4 = "";
//					mojianService.setappName("appName4", appName4);
                    setSharedPreference("appName4", appName4);
                    imgBtn4.setImageResource(R.drawable.xiaoyuan);
                }
            }
        });

        //Function 4 camera
        lLayoutFunc4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgBtn1Selected) {
                    appName1 = "com.sec.android.app.camera";
//					mojianService.setappName("appName1", appName1);
                    setSharedPreference("appName1", appName1);
                    setIconByPkgname(imgBtn1, appName1);
                }
                if (imgBtn2Selected) {
                    appName2 = "com.sec.android.app.camera";
//					mojianService.setappName("appName2", appName2);
                    setSharedPreference("appName2", appName2);
                    setIconByPkgname(imgBtn2, appName2);
                }
                if (imgBtn3Selected) {
                    appName3 = "com.sec.android.app.camera";
//					mojianService.setappName("appName3", appName3);
                    setSharedPreference("appName3", appName3);
                    setIconByPkgname(imgBtn3, appName3);
                }
                if (imgBtn4Selected) {
                    appName4 = "com.sec.android.app.camera";
//					mojianService.setappName("appName4", appName4);
                    setSharedPreference("appName4", appName4);
                    setIconByPkgname(imgBtn4, appName4);
                }
            }
        });

        //Function 6 voice recorder
        lLayoutFunc6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgBtn1Selected) {
                    appName1 = "com.sec.android.app.voicerecorder";
//							mojianService.setappName("appName1", appName1);
                    setSharedPreference("appName1", appName1);
                    setIconByPkgname(imgBtn1, appName1);
                }
                if (imgBtn2Selected) {
                    appName2 = "com.sec.android.app.voicerecorder";
//							mojianService.setappName("appName2", appName2);
                    setSharedPreference("appName2", appName2);
                    setIconByPkgname(imgBtn2, appName2);
                }
                if (imgBtn3Selected) {
                    appName3 = "com.sec.android.app.voicerecorder";
//							mojianService.setappName("appName3", appName3);
                    setSharedPreference("appName3", appName3);
                    setIconByPkgname(imgBtn3, appName3);
                }
                if (imgBtn4Selected) {
                    appName4 = "com.sec.android.app.voicerecorder";
//							mojianService.setappName("appName4", appName4);
                    setSharedPreference("appName4", appName4);
                    setIconByPkgname(imgBtn4, appName4);
                }
            }
        });
    }

    private void setSelectedBtn(ImageView imgView) {
        //selected is dayuan, the others are xiaoyuan
        imgBtn1.setBackgroundResource(R.drawable.xiaoyuan);
        imgBtn2.setBackgroundResource(R.drawable.xiaoyuan);
        imgBtn3.setBackgroundResource(R.drawable.xiaoyuan);
        imgBtn4.setBackgroundResource(R.drawable.xiaoyuan);
        imgView.setBackgroundResource(R.drawable.dayuan);
    }

    private void stopService() {
        Intent i = new Intent(MainActivity.this, MJService.class);
        this.stopService(i);
    }

    private void bindService() {
        Intent intent = new Intent("com.it51buy.mojian.MojianService");
        this.startService(intent);
        this.bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);

    }

    //调用bindService后 Service调用onBind()后 回调此函数
    private ServiceConnection myServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            mojianService = ((MJBinder) binder).getService();

        }

        public void onServiceDisconnected(ComponentName name) {
            mojianService = null;
        }

    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) { //这货就是上面那个int全局变量，下面的选择语句分别感觉不同的requesCode执行不同的语句。
            case 1:
                if (resultCode == RESULT_OK) {//RESULT_OK是结果码，可以自己改
                    Uri uriDate = data.getData();
                    appName1 = uriDate.toString();
                }
                //Toast.makeText(getApplicationContext(),appName1,Toast.LENGTH_SHORT).show();
                if (appName1 != "") {
//	    		mojianService.setappName("appName1", appName1);
                    setSharedPreference("appName1", appName1);
                    setIconByPkgname(imgBtn1, appName1);
                }

                break;

            case 2:
                if (resultCode == RESULT_OK) {//RESULT_OK是结果码，可以自己改
                    Uri uriDate = data.getData();
                    appName2 = uriDate.toString();
                }
                //Toast.makeText(getApplicationContext(),appName2,Toast.LENGTH_SHORT).show();
                if (appName2 != "") {
//	    		mojianService.setappName("appName2", appName2);
                    setSharedPreference("appName2", appName2);
                    setIconByPkgname(imgBtn2, appName2);
                }
                break;

            case 3:
                if (resultCode == RESULT_OK) {//RESULT_OK是结果码，可以自己改
                    Uri uriDate = data.getData();
                    appName3 = uriDate.toString();
                }
                //Toast.makeText(getApplicationContext(),appName3,Toast.LENGTH_SHORT).show();
                if (appName3 != "") {
//	    		mojianService.setappName("appName3", appName3);
                    setSharedPreference("appName3", appName3);
                    setIconByPkgname(imgBtn3, appName3);
                }
                break;

            case 4:
                if (resultCode == RESULT_OK) {//RESULT_OK是结果码，可以自己改
                    Uri uriDate = data.getData();
                    appName4 = uriDate.toString();
                }
                //Toast.makeText(getApplicationContext(),appName4,Toast.LENGTH_SHORT).show();
                if (appName4 != "") {
//	    		mojianService.setappName("appName4", appName4);
                    setSharedPreference("appName4", appName4);
                    setIconByPkgname(imgBtn4, appName4);
                }
                break;


        }
    }

    private void setSharedPreference(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("COM_IT51BUY_MOJIAN", MODE_WORLD_WRITEABLE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getSharedPreference(String key) {
        SharedPreferences pre = getSharedPreferences("COM_IT51BUY_MOJIAN", MODE_WORLD_READABLE);
        return (pre.getString(key, ""));
    }

    private void getIconByPackageName() {
        try {
            appName1 = getSharedPreference("appName1");
            appName2 = getSharedPreference("appName2");
            appName3 = getSharedPreference("appName3");
            appName4 = getSharedPreference("appName4");
        } catch (Exception e) {

        }

        try {
            if (appName1 != "") {
                appIcon1 = getPackageManager().getApplicationIcon(appName1);
                if (appIcon1.getIntrinsicWidth() > 75 || appIcon1.getIntrinsicHeight() > 75) {
                    appIcon1 = ImageUtils.zoomDrawable(appIcon1, 75, 75);
                }
                imgBtn1.setImageDrawable(appIcon1);

            }
            if (appName2 != "") {
                appIcon2 = getPackageManager().getApplicationIcon(appName2);
                if (appIcon2.getIntrinsicWidth() > 75 || appIcon2.getIntrinsicHeight() > 75) {
                    appIcon2 = ImageUtils.zoomDrawable(appIcon2, 75, 75);
                }
                imgBtn2.setImageDrawable(appIcon2);
            }
            if (appName3 != "") {
                appIcon3 = getPackageManager().getApplicationIcon(appName3);
                if (appIcon3.getIntrinsicWidth() > 75 || appIcon3.getIntrinsicHeight() > 75) {
                    appIcon3 = ImageUtils.zoomDrawable(appIcon3, 75, 75);
                }
                imgBtn3.setImageDrawable(appIcon3);
            }
            if (appName4 != "") {
                appIcon4 = getPackageManager().getApplicationIcon(appName4);
                if (appIcon4.getIntrinsicWidth() > 75 || appIcon4.getIntrinsicHeight() > 75) {
                    appIcon4 = ImageUtils.zoomDrawable(appIcon4, 75, 75);
                }
                imgBtn4.setImageDrawable(appIcon4);
            }

        } catch (Exception e) {

        }
    }

    private void setIconByPkgname(ImageView imgView, String pkgname) {
        try {
            Drawable appIcon = getPackageManager().getApplicationIcon(pkgname);
            if (appIcon.getIntrinsicWidth() > 75 || appIcon.getIntrinsicHeight() > 75) {
                appIcon = ImageUtils.zoomDrawable(appIcon, 75, 75);
            }
            imgView.setImageDrawable(appIcon);
        } catch (Exception e) {

        }
    }

    private long exitTime = 0;

    /**
     * Exit tips。
     */

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * Exit tip
     */
    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        this.unbindService(myServiceConnection);
    }

}
