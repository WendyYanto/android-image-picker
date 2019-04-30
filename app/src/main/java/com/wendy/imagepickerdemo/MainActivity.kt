package com.wendy.imagepickerdemo

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.wendy.imagepickerdemo.databinding.ActivityMainBinding
import com.wendy.imagepickerdemo.view.GalleryFragment

class MainActivity : AppCompatActivity() {

    private var activityBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 999)
        }

        activityBinding?.btShowGallery?.setOnClickListener {
            activityBinding?.flFragment.let { item ->
                item?.id?.let { fragmentLayoutId ->
                    supportFragmentManager.beginTransaction()
                        .add(fragmentLayoutId, GalleryFragment.getInstance(1 ))
                        .addToBackStack(null)
                        .commit()
                }
            }

            it.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        activityBinding?.btShowGallery?.let {
            if (it.visibility == View.GONE) {
                it.visibility = View.VISIBLE
            }
        }
    }
}
