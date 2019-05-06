package com.wendy.imagepickerdemo.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ActivityMainBinding
import com.wendy.imagepickerdemo.main.view.GalleryFragment
import com.wendy.imagepickerdemo.result.ResultActivity
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_NAME = "ImagePickerDemo"
    }

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
                        .add(fragmentLayoutId, GalleryFragment.getInstance(3))
                        .addToBackStack(null)
                        .commit()
                }
            }

            it.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toggleShowGalleryButtonVisibility()
    }

    fun getImageGalleryResultFromGalleryFragment(chosenImageList: MutableList<String>) {
        supportFragmentManager.popBackStackImmediate()
        supportActionBar?.title = TAG_NAME
        toggleShowGalleryButtonVisibility()

        if (chosenImageList.isNotEmpty()) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putStringArrayListExtra(
                ResultActivity.GET_IMAGE_GALLERY_RESULT,
                chosenImageList as ArrayList<String>
            )
            startActivity(intent)
        }

    }

    private fun toggleShowGalleryButtonVisibility() {
        activityBinding?.btShowGallery?.let {
            if (it.visibility == View.GONE) {
                it.visibility = View.VISIBLE
            }
        }
    }
}
