package com.example.alfamessanger.presentation.fragments.imageFullScreen

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentImageFullScreenBinding
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.presentation.BottomSheetApp
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.BottomSheetSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ImageFullScreenFragment : Fragment(), ViewPagerFullScreenAdapter.OnItemClickListenerFullScreen {

    private var binding: FragmentImageFullScreenBinding? = null
    private val mBinding get() = binding!!

    private var modelPhoto : SavedPhotoModel = SavedPhotoModel()
    private var listPosition = 0
    private var listPhotos : ArrayList<SavedPhotoModel> = ArrayList()
    private lateinit var mViewPager : ViewPager2
    private lateinit var mAdapter : ViewPagerFullScreenAdapter
    private var mSaved = false

    private lateinit var animOut : Animation
    private lateinit var animIn : Animation

    private val mViewModel: ImageFullScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageFullScreenBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY_MAIN.toolbar.visibility = View.GONE
        mBinding.fullScreenToolbar.visibility = View.VISIBLE
        initFields()
        APP_ACTIVITY_MAIN.navigation_view.visibility = View.GONE
    }

    private fun initFields(){
        setHasOptionsMenu(true)
        listPosition = arguments?.getInt(KEY_POSITION)!!
        listPhotos = arguments?.getParcelableArrayList<SavedPhotoModel>(KEY_CHAT_IMAGE) as ArrayList<SavedPhotoModel>
        mViewPager = mBinding.viewPagerFullScreen
        mAdapter = ViewPagerFullScreenAdapter(this){ model, position ->
            modelPhoto = model
            val positionAdapter = position + 1
            mBinding.currentPositionPhoto.text = positionAdapter.toString()
            mViewModel.checkSavedOrNot(model.image_url){
                mSaved = it
            }
        }
        mViewPager.adapter = mAdapter
        mViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mViewPager.doOnPreDraw {
            mViewPager.setCurrentItem(listPosition, false)
        }
        mAdapter.setList(listPhotos)
        mBinding.generalItemCount.text = listPhotos.size.toString()

        APP_ACTIVITY_MAIN.setSupportActionBar(mBinding.fullScreenToolbar)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowTitleEnabled(false)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.color_transparent_toolbar)
        mBinding.progressbar.visibility = View.GONE

        animOut =
            AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.toolbar_out)
        animOut.fillAfter = true
        animIn =
            AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.toolbar_in)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.full_screen_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.full_screen -> {
                if(!mSaved){
                    BottomSheetApp.create(BottomSheetSettings.FULL_SCREEN_IMAGE_SAVED){ typeAdd ->
                        when(typeAdd){
                            SAVE -> {
                                mBinding.progressbar.visibility = View.VISIBLE
                                mViewModel.saveImage(modelPhoto.image_url){
                                    mBinding.progressbar.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                else{
                    BottomSheetApp.create(BottomSheetSettings.FULL_SCREEN_IMAGE_NOT_SAVED){ typeAdd ->
                        when(typeAdd){
                            DELETE -> {
                                mBinding.progressbar.visibility = View.VISIBLE
                                mViewModel.deleteFromSaved(modelPhoto.image_url){
                                    mBinding.progressbar.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
            android.R.id.home -> {
                APP_ACTIVITY_MAIN.onBackPressed()
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY_MAIN.toolbar.visibility = View.VISIBLE
        mBinding.fullScreenToolbar.visibility = View.GONE
        APP_ACTIVITY_MAIN.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onItemClickAdapter() {
        if(mBinding.fullScreenToolbar.visibility == View.VISIBLE){
            mBinding.fullScreenToolbar.startAnimation(animOut)
            mBinding.fullScreenToolbar.animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    mBinding.fullScreenToolbar.visibility = View.GONE
                    mBinding.fullScreenToolbar.isEnabled = false
                    APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBar)
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })

        }
        else if(mBinding.fullScreenToolbar.visibility == View.GONE){
            mBinding.fullScreenToolbar.startAnimation(animIn)
            mBinding.fullScreenToolbar.visibility = View.VISIBLE
            mBinding.fullScreenToolbar.animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    mBinding.fullScreenToolbar.visibility = View.VISIBLE
                    mBinding.fullScreenToolbar.isEnabled =true
                    APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.color_transparent_toolbar)
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })
        }
    }

}