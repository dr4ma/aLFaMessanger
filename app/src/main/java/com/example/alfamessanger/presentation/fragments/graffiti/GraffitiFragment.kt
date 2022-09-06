package com.example.alfamessanger.presentation.fragments.graffiti

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.GraffitiFragmentBinding
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.BottomSheetApp
import com.example.alfamessanger.presentation.customViews.PaintView
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.BottomSheetSettings
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.example.alfamessanger.utills.listeners.SeekBarListener
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraffitiFragment : Fragment() {

    private var binding: GraffitiFragmentBinding? = null
    private val mBinding get() = binding!!

    private lateinit var mPaintView: PaintView
    private var colorBackground: Int = 0
    private var colorBrush: Int = 0
    private var sizeBrush: Int = 0
    private var sizeEraser: Int = 0
    private var flagSeekBar = false
    private var mUid : String = ""

    private lateinit var animFadeIn : Animation
    private lateinit var animFadeOut : Animation

    private val mViewModel: GraffitiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GraffitiFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initPaint()
        initFields()
    }

    private fun initPaint(){
        colorBackground = Color.TRANSPARENT
        colorBrush = resources.getColor(R.color.textColor)
        sizeEraser = 12
        sizeBrush = 12
        mBinding.seekBarBrush.progress = 24
        mPaintView = mBinding.drawing
        mPaintView.setBrushColor(colorBrush)
    }

    private fun initFields(){
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.graffiti_label))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        setHasOptionsMenu(true)
        mUid = arguments?.getString(KEY_UID).toString()
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mBinding.apply {
            brush.setColorFilter(ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorPlayer))
            animFadeIn = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, androidx.appcompat.R.anim.abc_fade_in)
            animFadeOut = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, androidx.appcompat.R.anim.abc_fade_out)

            erase.setOnClickListener {
                brush.setColorFilter(ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.textColor))
                erase.setColorFilter(ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorPlayer))
                mPaintView.enableEraser()
            }

            brush.setOnClickListener {
                erase.setColorFilter(ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.textColor))
                brush.setColorFilter(ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorPlayer))
                mPaintView.disableEraser()
            }

            seekBarBrush.setOnSeekBarChangeListener(SeekBarListener { progress ->
                sizeBrush = progress + 1
                mPaintView.setSizeBrush(sizeBrush)
            })

            sizeChange.setOnClickListener {
                if(!flagSeekBar){
                    seekBarContainer.startAnimation(animFadeIn)
                    seekBarContainer.visibility = View.VISIBLE
                    flagSeekBar = true
                }
                else{
                    seekBarContainer.startAnimation(animFadeOut)
                    seekBarContainer.visibility = View.GONE
                    flagSeekBar = false
                }
            }

            returnLastAction.setOnClickListener {
                mPaintView.returnLastAction()
            }

            changeColorBackGround.setOnClickListener {
                ColorPicker.showPicker(APP_ACTIVITY_MAIN.getString(R.string.choose_color_background), colorBrush){ lastSelectedColor ->
                    colorBackground = lastSelectedColor
                    mPaintView.setColorBackground(colorBackground)
                    changeColorBackGround.setColorFilter(lastSelectedColor)
                }
            }

            changeColorBrush.setOnClickListener {
                ColorPicker.showPicker(APP_ACTIVITY_MAIN.getString(R.string.choose_color), colorBrush){ lastSelectedColor ->
                    colorBrush = lastSelectedColor
                    mPaintView.setBrushColor(colorBrush)
                    changeColorBrush.setColorFilter(lastSelectedColor)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.graffiti_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                BottomSheetApp.create(BottomSheetSettings.GRAFFITI_BOTTOM_SHEET){ typeAdd ->
                    when(typeAdd){
                        SAVE_IN_GALLERY -> {
                            mViewModel.saveBitmap(mPaintView.getBitmap()){
                                showSnackBar(mBinding.brush, getString(R.string.graffiti_success_load), R.color.colorGreen)
                            }
                        }
                        CLEAR_CANVAS -> {
                            mPaintView.clearCanvas()
                        }
                    }
                }
            }
            R.id.send -> {
                if(mPaintView.getListActionsSize() != 0){
                    AppPreferences.setGraffiti(mViewModel.sendGraffitiInChat(mPaintView.getBitmap(), mUid))
                    APP_ACTIVITY_MAIN.onBackPressed()
                }
                else{
                    showToast(getString(R.string.canvas_is_clear_error))
                }
            }
            android.R.id.home -> {
                APP_ACTIVITY_MAIN.onBackPressed()
            }
        }
        return true
    }
}