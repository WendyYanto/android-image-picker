package com.wendy.imagepickerdemo.main.view

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
import com.wendy.imagepickerdemo.main.MainActivity
import com.wendy.imagepickerdemo.R
import com.wendy.imagepickerdemo.databinding.FragmentGalleryBinding
import com.wendy.imagepickerdemo.main.model.ImageGalleryUiModel
import com.wendy.imagepickerdemo.main.service.MediaHelper
import com.wendy.imagepickerdemo.main.view.adapter.ImageGalleryAdapter
import com.wendy.imagepickerdemo.main.view.adapter.ImageGalleryToolBarAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.IO

class GalleryFragment : Fragment() {

    private lateinit var galleryFragmentBinding: FragmentGalleryBinding
    private var imageGalleryAdapter: ImageGalleryAdapter? = null
    private var actionBar: ActionBar? = null
    private var categoryList: MutableList<String> = mutableListOf()
    private val chosenImageList: MutableList<String> = mutableListOf()
    private var imageGalleryUiModelList: HashMap<String, ArrayList<ImageGalleryUiModel>> = hashMapOf()
    private var maxCount: Int? = null
    private var currentCategoryIndex = 0

    companion object {
        private const val MAX_COUNT: String = "GALLERY_FRAGMENT_MAX_COUNT"
        fun getInstance(maxCount: Int): GalleryFragment {
            val galleryFragment = GalleryFragment()
            val bundle = Bundle()
            bundle.putInt(MAX_COUNT, maxCount)
            galleryFragment.arguments = bundle
            return galleryFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxCount = arguments?.getInt(MAX_COUNT, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        galleryFragmentBinding.btSubmit.setOnClickListener {
            val currentParentActivity = activity as MainActivity
            currentParentActivity.getImageGalleryResultFromGalleryFragment(chosenImageList)
        }

        actionBar = (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true)
        return galleryFragmentBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.gallery_toolbar_menu, menu)

        val spinnerMenuItem: MenuItem? = menu?.findItem(R.id.media_category_spinner)
        val spinner: Spinner = spinnerMenuItem?.actionView as Spinner

        activity?.let {
            val spinnerAdapter =
                ImageGalleryToolBarAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    categoryList
                )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentCategoryIndex = position
                    changeImageListInImageGalleryAdapter()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        chosenImageList.clear()
        updateTitleBar()
        getImageGalleryList()
    }

    private fun updateChosenImageList(imageUri: String, createAction: Boolean): Boolean {
        if (createAction) {
            maxCount?.let {
                if (chosenImageList.size < it) {
                    chosenImageList.add(imageUri)
                } else {
                    Toast.makeText(context, "You Cannot Select More Than $maxCount", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        } else {
            chosenImageList.remove(imageUri)
        }

        updateTitleBar()
        return true
    }

    private fun updateTitleBar() {
        actionBar?.title = "${chosenImageList.size}/$maxCount Selected"
    }

    private fun getImageGalleryList() {
        activity?.let {
            CoroutineScope(Dispatchers.Main).launch {
                categoryList.clear()
                imageGalleryUiModelList.clear()

                imageGalleryUiModelList = withContext(Dispatchers.IO) {
                    MediaHelper.getImageGallery(it)
                }

                if (imageGalleryUiModelList.isNotEmpty()) {
                    imageGalleryUiModelList.keys.forEach { key ->
                        categoryList.add(key)
                    }
                    it.invalidateOptionsMenu()
                    setUpImageGalleryAdapter()
                }
            }
        }
    }

    private fun changeImageListInImageGalleryAdapter() {
        imageGalleryAdapter?.changeItemsData(imageGalleryUiModelList[categoryList[currentCategoryIndex]] as MutableList<ImageGalleryUiModel>)
    }

    private fun setUpImageGalleryAdapter() {
        imageGalleryAdapter?.let {
            changeImageListInImageGalleryAdapter()
        } ?: run {
            galleryFragmentBinding.rvGridImage.apply {
                val gridLayoutManager = GridLayoutManager(
                    context,
                    3
                )
                layoutManager = gridLayoutManager
                if (imageGalleryAdapter == null) {
                    imageGalleryAdapter =
                        ImageGalleryAdapter(imageGalleryUiModelList[categoryList[currentCategoryIndex]] as MutableList<ImageGalleryUiModel>) { imageUri, createAction ->
                            updateChosenImageList(imageUri, createAction)
                        }
                }
                adapter = imageGalleryAdapter
            }
        }
    }
}