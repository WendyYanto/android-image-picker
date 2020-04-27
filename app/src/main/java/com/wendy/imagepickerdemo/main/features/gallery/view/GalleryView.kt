package com.wendy.imagepickerdemo.main.features.gallery.view

import com.wendy.imagepickerdemo.main.features.gallery.model.ImageGalleryUiModel

interface GalleryView {
    fun showImages(images: Map<String, List<ImageGalleryUiModel>>)
}