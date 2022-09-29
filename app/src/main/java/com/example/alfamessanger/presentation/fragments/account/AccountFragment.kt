package com.example.alfamessanger.presentation.fragments.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentAccountBinding
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.presentation.fragments.BaseFragment
import com.example.alfamessanger.presentation.BottomSheetApp
import com.example.alfamessanger.presentation.activities.registerActivity.RegisterActivity
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.BottomSheetSettings
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.util.ArrayList

@AndroidEntryPoint
class AccountFragment : BaseFragment() {

    private var binding: FragmentAccountBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: AccountFragmentViewModel by viewModels()

    private lateinit var mObserverLoadUser: Observer<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = getString(R.string.account)
        initFields()
        getUserModel()
        initListeners()
        initTheme()
        initStatus()
    }

    private fun initListeners() {
        mBinding.apply {
            accountBtnChangeUserName.setOnClickListener {
                APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_changeUserDataFragment)
            }
            accountBtnChangeBio.setOnClickListener {
                APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_changeUserDataFragment)
            }
            accountName.setOnClickListener {
                APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_changeUserDataFragment)
            }
            accountChangePhoto.setOnClickListener {
                CropImageBuilder.cropImage(this@AccountFragment, 200, 200, true)
            }
            accountUserPhoto.setOnClickListener {
                val bundle = Bundle()
                val model = SavedPhotoModel(image_url = USER.iconUrl)
                val list = mutableListOf(model)
                bundle.putParcelableArrayList(KEY_CHAT_IMAGE, list as ArrayList<SavedPhotoModel>)
                bundle.putInt(KEY_POSITION, 0)
                APP_ACTIVITY_MAIN.mNavController.navigate(
                    R.id.action_accountFragment_to_imageFullScreenFragment,
                    bundle
                )
            }
        }
    }

    private fun initFields() {
        setHasOptionsMenu(true)
        toolbarTools(ToolbarSettings.NORMAL_SETTINGS, getString(R.string.account))
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
    }

    private fun initTheme() {
        if (AppPreferences.getTheme() == NIGHT_THEME) {
            mBinding.switchSettingsTheme.isChecked = true
        }
        mBinding.switchSettingsTheme.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                APP_ACTIVITY_MAIN.delegate.applyDayNight()
                AppPreferences.setTheme(NIGHT_THEME)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                APP_ACTIVITY_MAIN.delegate.applyDayNight()
                AppPreferences.setTheme(LIGHT_THEME)
            }
        }
    }

    private fun initStatus(){
        if(AppPreferences.getStatus()){
            mBinding.switchSettingsStatus.isChecked = true
            mBinding.accountStatusTurn.text = getString(R.string.hide_mode_is_on)
        }
        else{
            mBinding.switchSettingsStatus.isChecked = false
            mBinding.accountStatusTurn.text = getString(R.string.hide_mode_is_off)
        }

        mBinding.switchSettingsStatus.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                AppPreferences.setHideStatus(true)
                mBinding.accountStatusTurn.text = getString(R.string.hide_mode_is_on)
            }
            else{
                AppPreferences.setHideStatus(false)
                mBinding.accountStatusTurn.text = getString(R.string.hide_mode_is_off)
            }
        }
    }

    private fun getUserModel() {
        mObserverLoadUser = Observer {
            if (it) {
                initUser()
                switcherProgressBar(false)
            }
        }
        switcherProgressBar(true)
        mViewModel.resultLoadUserdata.observe(APP_ACTIVITY_MAIN, mObserverLoadUser)
        mViewModel.getUserModel()
    }

    private fun initUser() {
        mBinding.apply {
            accountName.text = USER.name
            accountStatus.text = USER.status
            accountNickname.text = USER.nickname
            accountPhoneNumber.text = USER.phone
            accountBio.text = USER.bio
            accountUserPhoto.downloadAndSetImage(true, USER.iconUrl)
        }
    }

    private fun switcherProgressBar(load: Boolean) {
        mBinding.apply {
            if (load) {
                accountName.text = ""
                accountStatus.text = ""
                accountNickname.text = ""
                accountPhoneNumber.text = ""
                accountBio.text = ""
                progressBio.visibility = View.VISIBLE
                progressName.visibility = View.VISIBLE
                progressNickname.visibility = View.VISIBLE
                progressPhone.visibility = View.VISIBLE
            } else {
                progressBio.visibility = View.INVISIBLE
                progressName.visibility = View.INVISIBLE
                progressNickname.visibility = View.INVISIBLE
                progressPhone.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                BottomSheetApp.create(BottomSheetSettings.ACCOUNT_BOTTOM_SHEET){ typeAdd ->
                    when(typeAdd){
                        EXIT -> {
                            AUTH.signOut()
                            APP_ACTIVITY_MAIN.replaceActivity(APP_ACTIVITY_MAIN, RegisterActivity())
                        }
                        FRIENDS -> {
                            APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_friendsListFragment)
                        }
                        SAVED_PHOTOS -> {
                            APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_savedFragment)
                        }
                        BLACK_LIST -> {
                            APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_blackListFragment)
                        }
                        MY_CHANNELS -> {
                            APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_accountFragment_to_myChannelsFragment)
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            mBinding.progressIcon.visibility = View.VISIBLE
            val uri = CropImage.getActivityResult(data).uri
            val path = STORAGE_REFERENCE.child(FOLDER_PROFILE_IMAGE).child(UID)
            mViewModel.loadPic(uri, path) { url ->
                mBinding.accountUserPhoto.downloadAndSetImage(true, url)
                USER.iconUrl = url
                mBinding.progressIcon.visibility = View.INVISIBLE
            }
        }
    }
}