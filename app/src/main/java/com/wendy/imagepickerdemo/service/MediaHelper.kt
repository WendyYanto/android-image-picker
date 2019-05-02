package com.wendy.imagepickerdemo.service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.wendy.imagepickerdemo.model.ImageGalleryUiModel

class MediaHelper {

    companion object {
        fun getImageGallery(context: Context): HashMap<String, ArrayList<ImageGalleryUiModel>> {
            val imageGallery: HashMap<String, ArrayList<ImageGalleryUiModel>> = hashMapOf()

            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA
            )

            val images: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? = context.contentResolver.query(images, projection, null, null, null)

            println("Get Background Thread: ${Thread.currentThread().name}")
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
                    imageGallery[bucket]?.add(ImageGalleryUiModel(imageUri = uri))
                } while (cursor.moveToNext())
            }
            cursor?.close()
            return imageGallery
        }
    }

}