package dev.wendyyanto.imagepicker.dao

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import dev.wendyyanto.imagepicker.features.gallery.model.ImageGalleryUiModel

class MediaDao(private val context: Context) {

    fun getImageGallery(): Map<String, ArrayList<ImageGalleryUiModel>> {
        val fetchImageGalleryList: MutableMap<String, ArrayList<ImageGalleryUiModel>> =
            mutableMapOf()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )

        val images: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? =
            context.contentResolver.query(images, projection, null, null, null)

        try {
            if (cursor?.moveToFirst() == true) {
                var bucket: String
                var uri: String
                val bucketColumn: Int = cursor.getColumnIndex(projection[0])
                val imageUriColumn: Int = cursor.getColumnIndex(projection[1])

                do {
                    bucket = cursor.getString(bucketColumn)
                    uri = cursor.getString(imageUriColumn)
                    if (fetchImageGalleryList[bucket] == null) {
                        fetchImageGalleryList[bucket] = ArrayList()
                    }
                    if (uri.substring(
                            uri.length - 3,
                            uri.length
                        ) in ALLOWED_IMAGE_TYPE || uri.substring(
                            uri.length - 4,
                            uri.length
                        ) in ALLOWED_IMAGE_TYPE
                    ) {
                        fetchImageGalleryList[bucket]?.add(ImageGalleryUiModel(imageUri = uri))
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(MediaDao::javaClass.name, e.message)
        } finally {
            cursor?.close()
        }

        return fetchImageGalleryList
    }

    companion object {
        private val ALLOWED_IMAGE_TYPE = arrayOf("png", "jpg", "jpeg")
    }
}