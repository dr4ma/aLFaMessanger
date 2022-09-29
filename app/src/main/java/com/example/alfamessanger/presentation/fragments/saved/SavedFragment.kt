package com.example.alfamessanger.presentation.fragments.saved

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentSavedBinding
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {

    private var binding: FragmentSavedBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: SavedFragmentViewModel by viewModels()

    private lateinit var mAdapter: SavedAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserverPhotos: Observer<MutableList<SavedPhotoModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
        initObservers()
    }

    private fun initObservers() {
        mObserverPhotos = Observer {
            mRecyclerView.visibility = View.VISIBLE
            mBinding.photosProgress.visibility = View.GONE
            mBinding.noPhotos.visibility = View.GONE
            mAdapter.setList(it)
        }
        mViewModel.photos.observe(APP_ACTIVITY_MAIN, mObserverPhotos)
        mViewModel.getPhotos {
            mBinding.photosProgress.visibility = View.GONE
            mBinding.noPhotos.visibility = View.VISIBLE
            mAdapter.clearList()
        }
    }

    private fun initFields() {
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.saved_images))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mRecyclerView = mBinding.recyclerSaved
        mAdapter = SavedAdapter()
        val layoutManager = GridLayoutManager(APP_ACTIVITY_MAIN, 3)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
        mRecyclerView.visibility = View.INVISIBLE
        mBinding.noPhotos.visibility = View.GONE
        mBinding.photosProgress.visibility = View.VISIBLE
    }
}