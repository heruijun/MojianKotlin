package com.hrj.mojian.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.AsyncTask
import android.os.AsyncTask.Status
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.hrj.mojian.AppPreference
import com.hrj.mojian.R
import com.hrj.mojian.adapter.ListViewInvalidAppAdapter
import com.hrj.mojian.bean.MJBean
import kotlinx.android.synthetic.main.activity_invalid_app.*

class InvalidAppActivity : Activity() {

    private var mAdapter: ListViewInvalidAppAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invalid_app)
        initAdapter()
        initView()
        loadData()
    }

    private fun initAdapter() {
        mAdapter = ListViewInvalidAppAdapter(this)
        mAdapter!!.setNotifyOnChange(false)
    }

    private fun initView() {
        listview.adapter = mAdapter
        top_bar.setOnClickListener { back() }
    }

    private fun loadData() {
        if (mAdapter!!.count > 0) {
            return
        }

        if (mAsyncTask.status == Status.RUNNING) {
            return
        }

        mAsyncTask.execute()
    }

    private val mAsyncTask = object : AsyncTask<Void, Void, List<ResolveInfo>>() {

        lateinit var packageManager: PackageManager

        override fun onPreExecute() {
            progressbar!!.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void): List<ResolveInfo> {
            packageManager = getPackageManager()
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val apps = packageManager.queryIntentActivities(mainIntent, 0)
            return apps
        }

        override fun onPostExecute(result: List<ResolveInfo>?) {
            if (null != result) {
                for (info in result) {
                    val appName = info.loadLabel(packageManager).toString()
                    val packageIcon = info.iconResource
                    val packageName = info.activityInfo.packageName

                    if (TextUtils.isEmpty(appName) || TextUtils.isEmpty(packageName) || packageIcon == 0) {
                        continue
                    }

                    val isUsable = AppPreference.getBoolean(packageName, true)
                    val mjItem = MJBean(MJBean.APP_TYPE, appName, packageName, isUsable)
                    mAdapter!!.add(mjItem)
                }
            }

            mAdapter!!.notifyDataSetChanged()
            progressbar.visibility = View.GONE
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun back() {
        finish()
    }
}
