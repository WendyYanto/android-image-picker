package com.wendy.imagepickerdemo.main.features.gallery.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.FragmentGalleryBinding
import com.wendy.imagepickerdemo.main.di.Injector
import com.wendy.imagepickerdemo.main.features.gallery.model.ImageGalleryUiModel
import com.wendy.imagepickerdemo.main.features.gallery.adapter.ImageGalleryAdapter
import com.wendy.imagepickerdemo.main.features.gallery.adapter.ImageGalleryToolBarAdapter
import com.wendy.imagepickerdemo.main.features.gallery.presenter.GalleryPresenter
import com.wendy.imagepickerdemo.main.features.gallery.presenter.GalleryPresenterImpl
import com.wendy.imagepickerdemo.main.features.main.MainActivity

class GalleryFragment : Fragment(), GalleryView {

    private lateinit var galleryFragmentBinding: FragmentGalleryBinding
    private lateinit var galleryPresenter: GalleryPresenter
    private var adapter: ImageGalleryAdapter? = null
    private var actionBar: ActionBar? = null

    private var categories: MutableList<String> = mutableListOf()
    private val chosenImages: MutableList<String> = mutableListOf()
    private var images: MutableMap<String, List<ImageGalleryUiModel>> =
        mutableMapOf()
    private var maxCount: Int? = null
    private var categoryIndex = 0

    companion object {
        private const val MAX_COUNT: String = "GALLERY_FRAGMENT_MAX_COUNT"
        fun getInstance(maxCount: Int): GalleryFragment {
            val galleryFragment =
                GalleryFragment()
            val bundle = Bundle()
            bundle.putInt(MAX_COUNT, maxCount)
            galleryFragment.arguments = bundle
            return galleryFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxCount = arguments?.getInt(MAX_COUNT, 0)
        initPresenter()
    }

    private fun initPresenter() {
        Injector.store(
            GalleryPresenter::class.java,
            GalleryPresenterImpl(this, Injector.get())
        )
        galleryPresenter = Injector.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        galleryFragmentBinding.btSubmit.setOnClickListener {
            val currentParentActivity = activity as MainActivity
            currentParentActivity.getImageGalleryResultFromGalleryFragment(chosenImages)
        }

        actionBar = (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true)
        return this.galleryFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        val galleryAdapter = ImageGalleryAdapter(::updateChosenImages)
        this.adapter = galleryAdapter
        with(galleryFragmentBinding.rvGridImage) {
            layoutManager = GridLayoutManager(
                context,
                3
            )
            adapter = galleryAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.gallery_toolbar_menu, menu)
        setupToolbarAdapter(menu)
    }

    private fun setupToolbarAdapter(menu: Menu?) {
        val spinnerMenuItem: MenuItem? = menu?.findItem(R.id.media_category_spinner)
        val spinner: Spinner = spinnerMenuItem?.actionView as Spinner

        activity?.let {
            val spinnerAdapter =
                ImageGalleryToolBarAdapter(
                    it,
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
                        context,
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
        adapter?.submitList(images[categories[categoryIndex]]?.toMutableList())
    }

    override fun showImages(images: Map<String, List<ImageGalleryUiModel>>) {
        activity?.let {
            categories.clear()
            this.images.clear()
            this.images.putAll(images)
            if (this.images.isNotEmpty()) {
                categories.addAll(images.keys)
                it.invalidateOptionsMenu()
                populateImages()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        galleryPresenter.detach()
        Injector.remove(GalleryPresenter::class.java)
    }
}