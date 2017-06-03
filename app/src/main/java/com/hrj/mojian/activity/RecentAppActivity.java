package com.hrj.mojian.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hrj.mojian.R;
import com.hrj.mojian.utils.MJToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecentAppActivity extends Activity {

    private int MAX_RECENT_TASKS = 12; // allow for some discards
    private int repeatCount = 12;// 保证上面两个值相等
    // 用来存放每一个recentApplication的信息，我们这里存放应用程序名，应用程序图标和intent。
    private List<HashMap<String, Object>> mAppInfos = new ArrayList<HashMap<String, Object>>();
    private Dialog mDialog;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInflater();
        reloadButtons();
        generateDialog();
    }

    private void initInflater() {
        mInflater = LayoutInflater.from(this);
    }

    /**
     * 核心方法，加载最近启动的应用程序 注意：这里我们取出的最近任务为 MAX_RECENT_TASKS +
     * 1个，因为有可能最近任务中包好Launcher2。 这样可以保证我们展示出来的 最近任务 为 MAX_RECENT_TASKS 个
     */
    private void reloadButtons() {
        final PackageManager pm = getPackageManager();
        final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // 从ActivityManager中取出用户最近launch过的 MAX_RECENT_TASKS + 1
        // 个，以从早到晚的时间排序，
        // 注意这个
        // 0x0002,它的值在launcher中是用ActivityManager.RECENT_IGNORE_UNAVAILABLE
        // 但是这是一个隐藏域，因此我把它的值直接拷贝到这里
        final List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(MAX_RECENT_TASKS + 1, 0x0002);

        // 这个activity的信息是我们的launcher
        ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);
        int numTasks = recentTasks.size();
        for (int i = 0; i < numTasks && (i < MAX_RECENT_TASKS); i++) {
            HashMap<String, Object> singleAppInfo = new HashMap<String, Object>();// 当个启动过的应用程序的信息
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }

            // 如果找到是launcher，直接continue，后面的appInfos.add操作就不会发生了
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(intent.getComponent().getPackageName()) && homeInfo.name.equals(intent.getComponent().getClassName())) {
                    MAX_RECENT_TASKS = MAX_RECENT_TASKS + 1;
                    continue;
                }
            }

            // 设置intent的启动方式为 创建新task()【并不一定会创建】
            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) | Intent.FLAG_ACTIVITY_NEW_TASK);

            // 获取指定应用程序activity的信息(按我的理解是：某一个应用程序的最后一个在前台出现过的activity。)
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

            if (null == resolveInfo) {
                continue;
            }

            final ActivityInfo activityInfo = resolveInfo.activityInfo;
            final String title = activityInfo.loadLabel(pm).toString();
            Drawable icon = activityInfo.loadIcon(pm);

            if (title != null && title.length() > 0 && icon != null) {
                singleAppInfo.put("title", title);
                singleAppInfo.put("icon", icon);
                singleAppInfo.put("tag", intent);
            }

            mAppInfos.add(singleAppInfo);
        }
        MAX_RECENT_TASKS = repeatCount;
    }

    /**
     * 弹出最近任务对话框
     */
    public void generateDialog() {
        mDialog = new Dialog(this, R.style.dialog);
        View dialogView = mInflater.inflate(R.layout.dialog_recent_app, null);
        final GridView mGridView = (GridView) dialogView.findViewById(R.id.dialog_gridview);
        mGridView.setAdapter(new RecentAppAdapter());
        mDialog.setContentView(dialogView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog = null;
                mAppInfos.clear();
                finish();
            }
        });
        mDialog.show();
    }

    private class RecentAppAdapter implements ListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public int getCount() {
            return mAppInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View infoView = mInflater.inflate(R.layout.gridview_recent_app_item, null);
            ImageView mImageView = (ImageView) infoView.findViewById(R.id.app_icon);
            TextView mTextView = (TextView) infoView.findViewById(R.id.app_info);
            String title = (String) mAppInfos.get(position).get("title");
            Drawable icon = (Drawable) mAppInfos.get(position).get("icon");
            Intent singleIntent = (Intent) mAppInfos.get(position).get("tag");
            infoView.setTag(singleIntent);
            mImageView.setImageDrawable(icon);
            mTextView.setText(title);
            infoView.setOnClickListener(new SingleAppClickListener());
            return infoView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }

    class SingleAppClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = (Intent) v.getTag();
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    MJToast.show(R.string.no_launch_recent_task, Toast.LENGTH_SHORT);
                } finally {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            }
        }

    }

}