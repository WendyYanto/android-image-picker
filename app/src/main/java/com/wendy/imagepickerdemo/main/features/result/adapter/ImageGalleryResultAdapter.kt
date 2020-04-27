package com.wendy.imagepickerdemo.main.features.result.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ImagePickerItemsBinding

class ImageGalleryResultAdapter(private val items: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val imageResultItem = LayoutInflater.from(parent.context).inflate(R.layout.image_picker_items, parent, false)
        return ImageResultViewHolder(imageResultItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageGalleryViewHolder = holder as ImageResultViewHolder
        val imageUri = items[position]
        imageGalleryViewHolder.bind(imageUri)
    }

    inner class ImageResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding: ImagePickerItemsBinding? = DataBindingUtil.bind(itemView)

        fun bind(imageUri: String) {
            viewBinding?.cbImageItem?.visibility = View.GONE
            viewBinding?.ivPhoto?.let {
                it.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(itemView).load(imageUri).placeholder(R.drawable.image_placeholder).into(it)
            }
        }

    }
}