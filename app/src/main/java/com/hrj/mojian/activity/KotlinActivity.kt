package com.hrj.mojian.activity

import android.app.Activity
import android.os.Bundle
import com.hrj.mojian.R
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        init()
    }

    private fun init() {
        ktext.setText("你好")
    }
}
