package com.wendy.imagepickerdemo.main.features.result

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wendy.imagepickerdemo.databinding.ActivityResultBinding
import com.wendy.imagepickerdemo.main.features.result.adapter.ImageGalleryResultAdapter

class ResultActivity : AppCompatActivity() {

    companion object {
        const val GET_IMAGE_GALLERY_RESULT = "GET_IMAGE_GALLERY_RESULT"
    }

    private lateinit var imageGalleryResultAdapter: ImageGalleryResultAdapter

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val results = intent.getStringArrayListExtra(GET_IMAGE_GALLERY_RESULT)
        imageGalleryResultAdapter = ImageGalleryResultAdapter()
        with(binding.rvImageResult) {
            layoutManager = LinearLayoutManager(context)
            adapter = imageGalleryResultAdapter
        }
        imageGalleryResultAdapter.submitList(results)
    }
}
