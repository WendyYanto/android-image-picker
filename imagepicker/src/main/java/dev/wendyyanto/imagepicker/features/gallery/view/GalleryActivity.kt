package dev.wendyyanto.imagepicker.features.gallery.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import dev.wendyyanto.imagepicker.R
import dev.wendyyanto.imagepicker.di.Injector
import dev.wendyyanto.imagepicker.features.gallery.model.ImageGalleryUiModel
import dev.wendyyanto.imagepicker.features.gallery.adapter.ImageGalleryAdapter
import dev.wendyyanto.imagepicker.features.gallery.adapter.ImageGalleryToolBarAdapter
import dev.wendyyanto.imagepicker.features.gallery.presenter.GalleryPresenter
import dev.wendyyanto.imagepicker.features.gallery.presenter.GalleryPresenterImpl
import dev.wendyyanto.imagepicker.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity(), GalleryView {

    private lateinit var activityGalleryBinding: ActivityGalleryBinding
    private lateinit var galleryPresenter: GalleryPresenter
    private lateinit var adapter: ImageGalleryAdapter
    private var actionBar: ActionBar? = null

    private var categories: MutableList<String> = mutableListOf()
    private val chosenImages: ArrayList<String> = arrayListOf()
    private var images: MutableMap<String, List<ImageGalleryUiModel>> =
        mutableMapOf()
    private var maxCount: Int? = null
    private var categoryIndex = 0

    companion object {
        const val MAX_COUNT = "GALLERY_MAX_COUNT"
        const val RESULT = "GALLERY_RESULT"
        private const val READ_EXTERNAL_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxCount = intent.getIntExtra(MAX_COUNT, 1)
        activityGalleryBinding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(activityGalleryBinding.root)
        setupAdapter()

        if (checkExternalStoragePermission()) {
            initPresenter()
        }

        activityGalleryBinding.btSubmit.setOnClickListener {
            val data = Intent()
            data.putStringArrayListExtra(RESULT, chosenImages)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun initPresenter() {
        Injector.store(
            GalleryPresenter::class.java,
            GalleryPresenterImpl(this, Injector.get())
        )
        galleryPresenter = Injector.get()
    }

    private fun setupAdapter() {
        val galleryAdapter = ImageGalleryAdapter(::updateChosenImages)
        this.adapter = galleryAdapter
        with(activityGalleryBinding.rvGridImage) {
            layoutManager = GridLayoutManager(this@GalleryActivity, 3)
            adapter = galleryAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gallery_toolbar_menu, menu)
        setupToolbarAdapter(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupToolbarAdapter(menu: Menu?) {
        val spinnerMenuItem: MenuItem? = menu?.findItem(R.id.media_category_spinner)
        val spinner: Spinner = spinnerMenuItem?.actionView as Spinner

        val spinnerAdapter =
            ImageGalleryToolBarAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryIndex = position
                if (images.isNotEmpty()) {
                    populateImages()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        chosenImages.clear()
        updateTitleBar()
        galleryPresenter.fetchImages()
    }

    private fun updateChosenImages(imageUri: String, createAction: Boolean): Boolean {
        if (createAction) {
            maxCount?.let {
                if (chosenImages.size < it) {
                    chosenImages.add(imageUri)
                } else {
                    Toast.makeText(
                        this,
                        "You Cannot Select More Than $maxCount",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
        } else {
            chosenImages.remove(imageUri)
        }
        updateTitleBar()
        return true
    }

    private fun updateTitleBar() {
        actionBar?.title = "${chosenImages.size}/$maxCount Selected"
    }

    private fun populateImages() {
        adapter.submitList(images[categories[categoryIndex]]?.toMutableList())
    }

    override fun showImages(images: Map<String, List<ImageGalleryUiModel>>) {
        categories.clear()
        this.images.clear()
        this.images.putAll(images)
        if (this.images.isNotEmpty()) {
            categories.addAll(images.keys)
            invalidateOptionsMenu()
            populateImages()
        }
    }

    override fun onStop() {
        super.onStop()
        galleryPresenter.detach()
        Injector.remove(GalleryPresenter::class.java)
    }

    private fun checkExternalStoragePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_PERMISSION_REQUEST_CODE
            )
        } else {
            return true
        }
        return false
    }
}