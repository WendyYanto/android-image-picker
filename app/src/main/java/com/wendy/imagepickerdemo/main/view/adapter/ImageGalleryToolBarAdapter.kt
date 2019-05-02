package com.wendy.imagepickerdemo.main.view.adapter

import android.content.Context
import android.widget.ArrayAdapter

class ImageGalleryToolBarAdapter(context: Context, resource: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, resource, objects)