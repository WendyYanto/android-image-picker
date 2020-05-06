package com.wendy.imagepickerdemo.main.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ActivityMainBinding
import com.wendy.imagepickerdemo.main.features.result.ResultActivity
import dev.wendyyanto.imagepicker.features.gallery.view.GalleryActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
    }

    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        activityBinding.btShowGallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(GalleryActivity.SUBMIT_BUTTON_STYLE, R.style.MyButton)
            intent.putExtra(GalleryActivity.THEME, R.style.GalleryTheme)
            intent.putExtra(GalleryActivity.CATEGORY_DROPDOWN_ITEM_LAYOUT, R.layout.spinner_item)
            intent.putExtra(GalleryActivity.MAX_COUNT, 3)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        data?.let { intent ->
            when (requestCode) {
                GALLERY_REQUEST_CODE -> showResults(intent.getStringArrayListExtra(GalleryActivity.RESULT))
            }
        }
    }

    private fun showResults(chosenImageList: MutableList<String>) {
        if (chosenImageList.isNotEmpty()) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putStringArrayListExtra(
                ResultActivity.GET_IMAGE_GALLERY_RESULT,
                chosenImageList as ArrayList<String>
            )
            startActivity(intent)
        }
    }
}
