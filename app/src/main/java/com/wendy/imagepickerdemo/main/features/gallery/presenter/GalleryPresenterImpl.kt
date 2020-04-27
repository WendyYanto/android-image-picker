package com.wendy.imagepickerdemo.main.features.gallery.presenter

import com.wendy.imagepickerdemo.main.dao.MediaDao
import com.wendy.imagepickerdemo.main.features.gallery.view.GalleryView
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main

class GalleryPresenterImpl(private val view: GalleryView, private val mediaDao: MediaDao) :
    GalleryPresenter {

    private var job: Job? = null

    override fun fetchImages() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val results = mediaDao.getImageGallery() as MutableMap
            withContext(Dispatchers.Main) {
                view.showImages(results)
            }
        }
    }

    override fun detach() {
        job?.cancel()
    }

}