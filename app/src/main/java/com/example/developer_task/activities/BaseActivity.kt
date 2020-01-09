package com.example.developer_task.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun getLayoutForActivity(): Int
    protected abstract fun onActivityReady()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutForActivity())
        onActivityReady()
    }
}