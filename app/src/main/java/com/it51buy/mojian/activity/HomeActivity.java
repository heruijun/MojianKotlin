package com.it51buy.mojian.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.it51buy.mojian.AppPreference;
import com.it51buy.mojian.FileLog;
import com.it51buy.mojian.R;
import com.it51buy.mojian.utils.Define;
import com.it51buy.mojian.utils.DisplayUtil;
import com.it51buy.mojian.view.FunctionView;
import com.it51buy.mojian.view.MJView;
import com.it51buy.mojian.view.OperationView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends Activity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private View mRoot;
    private static final int PERMISSON_REQUESTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRoot = findViewById(R.id.root);
        initView();
        mRoot.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkShowFloatPrompt();
            }
        }, 200);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodePermissions();
    }

    private void initView() {
        findViewById(R.id.setting).setOnClickListener(this);
        new FunctionView(mRoot);
        new MJView(mRoot);
        new OperationView(mRoot);
    }

    private void checkShowFloatPrompt() {
        boolean isMiPhone = DisplayUtil.isMiPhone(this);
        if (!isMiPhone) {
            return;
        }

        boolean hasShow = AppPreference.getBoolean(Define.PREFERENCES_FLOAT_PROMPT, false);
        if (hasShow) {
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.dialog).
                setMessage(R.string.float_dialog_message).setPositiveButton(R.string.float_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getPackageName()));
                startActivity(i);
            }
        }).setNegativeButton(R.string.float_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        AppPreference.setBoolean(Define.PREFERENCES_FLOAT_PROMPT, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileLog.deleteExpiredLog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_FRAME_BUFFER,
            Manifest.permission.WRITE_SECURE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE
    };

    @AfterPermissionGranted(PERMISSON_REQUESTCODE)
    private void requestCodePermissions() {
        if (!EasyPermissions.hasPermissions(this, needPermissions)) {
            EasyPermissions.requestPermissions(this, "应用需要这些权限", PERMISSON_REQUESTCODE, needPermissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        recreate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        showMissingPermissionDialog();
    }

    private void showMissingPermissionDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
