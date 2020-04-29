package com.wendy.imagepickerdemo.main.features.result.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ItemImageBinding

class ImageGalleryResultAdapter :
    ListAdapter<String, ImageGalleryResultAdapter.ImageResultViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageResultViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageResultViewHolder, position: Int) {
        val imageUri = getItem(position)
        holder.bind(imageUri)
    }

    inner class ImageResultViewHolder(private val viewBinding: ItemImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(imageUri: String) {
            viewBinding.cbImageItem.visibility = View.GONE
            viewBinding.ivPhoto.let {
                it.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(itemView).load(imageUri).placeholder(R.drawable.image_placeholder)
                    .into(it)
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(old: String, new: String): Boolean {
                return old == new
            }

            override fun areContentsTheSame(old: String, new: String): Boolean {
                return old == new
            }
        }
    }
}