package com.wendy.imagepickerdemo.main.features.result

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ActivityResultBinding
import com.wendy.imagepickerdemo.main.features.result.adapter.ImageGalleryResultAdapter

class ResultActivity : AppCompatActivity() {

    companion object {
        const val GET_IMAGE_GALLERY_RESULT = "GET_IMAGE_GALLERY_RESULT"
    }

    private lateinit var imageGalleryResultAdapter: ImageGalleryResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityResultBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_result)
        val results = intent.getStringArrayListExtra(GET_IMAGE_GALLERY_RESULT)
        imageGalleryResultAdapter = ImageGalleryResultAdapter()
        with(binding.rvImageResult) {
            layoutManager = LinearLayoutManager(context)
            adapter = imageGalleryResultAdapter
        }
        imageGalleryResultAdapter.submitList(results)
    }
}
