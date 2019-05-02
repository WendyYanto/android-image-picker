package com.wendy.imagepickerdemo.main.service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.wendy.imagepickerdemo.main.model.ImageGalleryUiModel

class MediaHelper {

    companion object {

        private val ALLOWED_IMAGE_TYPE = arrayOf("png", "jpg", "jpeg")

        fun getImageGallery(context: Context): HashMap<String, ArrayList<ImageGalleryUiModel>> {
            val imageGallery: HashMap<String, ArrayList<ImageGalleryUiModel>> = hashMapOf()
            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA
            )

            val images: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? = context.contentResolver.query(images, projection, null, null, null)

            if (cursor?.moveToFirst() == true) {
                var bucket: String
                var uri: String
                val bucketColumn: Int = cursor.getColumnIndex(projection[0])
                val imageUriColumn: Int = cursor.getColumnIndex(projection[1])

                do {
                    bucket = cursor.getString(bucketColumn)
                    uri = cursor.getString(imageUriColumn)
                    if (imageGallery[bucket] == null) {
                        imageGallery[bucket] = ArrayList()
                    }
                    if (uri.substring(uri.length - 3, uri.length) in ALLOWED_IMAGE_TYPE) {
                        imageGallery[bucket]?.add(ImageGalleryUiModel(imageUri = uri))
                    }
                } while (cursor.moveToNext())
            }
            cursor?.close()
            return imageGallery
        }
    }

}