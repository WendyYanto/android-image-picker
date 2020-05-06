package dev.wendyyanto.imagepicker.features.gallery.presenter

import dev.wendyyanto.imagepicker.features.base.BasePresenter

interface GalleryPresenter : BasePresenter {
    fun fetchImages()
}