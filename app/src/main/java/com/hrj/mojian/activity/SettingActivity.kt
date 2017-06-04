package com.hrj.mojian.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hrj.mojian.App
import com.hrj.mojian.AppPreference
import com.hrj.mojian.R
import com.hrj.mojian.utils.Define
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }

    private fun initView() {
        key_vibration_toggle!!.isChecked = AppPreference.getBoolean(Define.PREFERENCES_TURNOVER_INVALID, true)

        turnover_invalid_toggle!!.isChecked = AppPreference.getBoolean(Define.PREFERENCES_TURNOVER_INVALID, true)
        notification_toggle!!.isChecked = AppPreference.getBoolean(Define.PREFERENCES_NOTIFICATION, true)

        key_vibration_toggle!!.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setBoolean(Define.PREFERENCES_KEY_VIBRATION, key_vibration_toggle!!.isChecked)
            App.getInstance().setKeyVibrationToggle(key_vibration_toggle!!.isChecked)
        }

        turnover_invalid_toggle!!.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setBoolean(Define.PREFERENCES_TURNOVER_INVALID, turnover_invalid_toggle!!.isChecked)
            App.getInstance().setTurnoverInvalidToggle(turnover_invalid_toggle!!.isChecked)
        }

        notification_toggle!!.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setBoolean(Define.PREFERENCES_NOTIFICATION, notification_toggle!!.isChecked)
            App.getInstance().setNotificationToggle(notification_toggle!!.isChecked)
        }

        application_invalid_view!!.setOnClickListener {
            val intent = Intent(this, InvalidAppActivity::class.java)
            startActivity(intent)
        }

        back.setOnClickListener { back() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        back()
    }

    private fun back() {
        finish()
    }

}