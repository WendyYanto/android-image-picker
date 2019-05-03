package com.wendy.imagepickerdemo.main.service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.wendy.imagepickerdemo.main.model.ImageGalleryUiModel
import kotlinx.coroutines.*

class MediaHelper {

    companion object {

        private val ALLOWED_IMAGE_TYPE = arrayOf("png", "jpg", "jpeg")
        private var finalGalleryImageList: HashMap<String, ArrayList<ImageGalleryUiModel>> = hashMapOf()

        suspend fun getImageGallery(context: Context): HashMap<String, ArrayList<ImageGalleryUiModel>> {
            withContext(Dispatchers.Default) {
                val imageGalleryList = async {
                    val fetchImageGalleryList: HashMap<String, ArrayList<ImageGalleryUiModel>> = hashMapOf()
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
                            if (fetchImageGalleryList[bucket] == null) {
                                fetchImageGalleryList[bucket] = ArrayList()
                            }
                            if (uri.substring(uri.length - 3, uri.length) in ALLOWED_IMAGE_TYPE) {
                                fetchImageGalleryList[bucket]?.add(ImageGalleryUiModel(imageUri = uri))
                            }
                        } while (cursor.moveToNext())
                    }
                    cursor?.close()
                    return@async fetchImageGalleryList
                }
                finalGalleryImageList = imageGalleryList.await()
            }
            return finalGalleryImageList
        }

    }

}