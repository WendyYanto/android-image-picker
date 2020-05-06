package dev.wendyyanto.imagepicker.features.gallery.adapter

import android.content.Context
import android.widget.ArrayAdapter

internal class ImageGalleryToolBarAdapter(context: Context, resource: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, resource, objects)