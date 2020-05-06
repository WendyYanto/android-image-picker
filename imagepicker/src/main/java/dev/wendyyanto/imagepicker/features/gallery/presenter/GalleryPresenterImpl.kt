package dev.wendyyanto.imagepicker.features.gallery.presenter

import dev.wendyyanto.imagepicker.dao.MediaDao
import dev.wendyyanto.imagepicker.features.gallery.view.GalleryView
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