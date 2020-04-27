package com.wendy.imagepickerdemo.main.features.gallery.adapter

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.ImagePickerItemsBinding
import com.wendy.imagepickerdemo.main.features.gallery.model.ImageGalleryUiModel

class ImageGalleryAdapter(
    private var items: MutableList<ImageGalleryUiModel>,
    private val listener: (imageUri: String, createAction: Boolean) -> Boolean
) : ListAdapter<ImageGalleryUiModel, ImageGalleryAdapter.ImageGalleryViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryViewHolder {
        return ImageGalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_picker_items, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ImageGalleryViewHolder, position: Int) {
        val imageGalleryViewHolder: ImageGalleryViewHolder = holder
        val imageUri = items[position]
        imageGalleryViewHolder.bind(imageUri)
    }

    inner class ImageGalleryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val viewBinding: ImagePickerItemsBinding? = DataBindingUtil.bind(itemView)
        private lateinit var imageGalleryUiModel: ImageGalleryUiModel

        fun bind(imageGalleryUiModel: ImageGalleryUiModel) {
            this.imageGalleryUiModel = imageGalleryUiModel
            viewBinding?.ivPhoto?.let {
                Glide.with(itemView).load(imageGalleryUiModel.imageUri)
                    .placeholder(R.drawable.image_placeholder).into(
                        it
                    )
                it.setOnClickListener(this)
            }
            viewBinding?.cbImageItem?.let {
                if (imageGalleryUiModel.isChecked) {
                    it.isChecked = true
                    return
                }
                it.isChecked = false
            }
        }

        override fun onClick(v: View?) {
            viewBinding?.cbImageItem?.let {
                if (it.isChecked && listener.invoke(this.imageGalleryUiModel.imageUri, false)) {
                    updateCheckBox(it, false)
                } else if (listener.invoke(this.imageGalleryUiModel.imageUri, true)) {
                    updateCheckBox(it, true)
                }
            }
        }

        private fun updateCheckBox(checkBox: CheckBox, state: Boolean) {
            this.imageGalleryUiModel.isChecked = state
            checkBox.isChecked = state
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<ImageGalleryUiModel>() {
            override fun areItemsTheSame(
                oldImage: ImageGalleryUiModel,
                newImage: ImageGalleryUiModel
            ): Boolean {
                return oldImage == newImage
            }

            override fun areContentsTheSame(
                oldImage: ImageGalleryUiModel,
                newImage: ImageGalleryUiModel
            ): Boolean {
                return oldImage.imageUri == newImage.imageUri && oldImage.isChecked == newImage.isChecked
            }

        }
    }
}