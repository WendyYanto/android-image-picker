package com.wendy.imagepickerdemo.main.features.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ActivityMainBinding
import com.wendy.imagepickerdemo.main.features.gallery.view.GalleryFragment
import com.wendy.imagepickerdemo.main.features.result.ResultActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_NAME = "ImagePickerDemo"
        private const val READ_EXTERNAL_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        activityBinding.btShowGallery.setOnClickListener {
            if (checkExternalStoragePermission()) {
                activityBinding.flFragment.let { item ->
                    item.id.let { fragmentLayoutId ->
                        supportFragmentManager.beginTransaction()
                            .add(
                                fragmentLayoutId,
                                GalleryFragment.getInstance(
                                    3
                                )
                            )
                            .addToBackStack(null)
                            .commit()
                    }
                }
                it.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toggleShowGalleryButtonVisibility()
    }

    fun showResults(chosenImageList: MutableList<String>) {
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
        activityBinding.btShowGallery.let {
            if (it.visibility == View.GONE) {
                it.visibility = View.VISIBLE
                supportActionBar?.title = getString(R.string.app_name)
                supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_SHOW_TITLE
            } else {
                it.visibility = View.GONE
            }
        }
    }

    private fun checkExternalStoragePermission(): Boolean {
        var permission = false
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_PERMISSION_REQUEST_CODE
            )
        } else {
            permission = true
        }
        return permission
    }
}
