package com.example.alfamessanger.presentation.fragments.userProfile

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentUserProfileBinding
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.DialogApp
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.DialogSettings
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var binding: FragmentUserProfileBinding? = null
    private val mBinding get() = binding!!

    private lateinit var mUserModel: UserModel
    private val mViewModel: UserProfileViewModel by viewModels()

    private var checkFriend = false
    private var checkBlock = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, mUserModel.nickname)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initFields() {
        mBinding.apply {
            APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
            APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
            APP_ACTIVITY_MAIN.window.statusBarColor =
                ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)
            mUserModel = arguments?.getSerializable(KEY_PROFILE) as UserModel
            APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
            if (mUserModel.iconUrl != "") {
                userPhotoProfile.downloadAndSetImage(true, mUserModel.iconUrl)
            } else {
                userPhotoProfile.setImageDrawable(
                    APP_ACTIVITY_MAIN.resources.getDrawable(
                        R.drawable.default_user,
                        APP_ACTIVITY_MAIN.theme
                    )
                )
            }

            mViewModel.checkFriendOrNot(mUserModel) {
                checkFriend = it
                if (checkFriend) {
                    addInFriendLabel.text = getString(R.string.remove_from_friends)
                } else {
                    addInFriendLabel.text = getString(R.string.add_in_friend)
                }

                if (mUserModel.privateAccount && !checkFriend) {
                    userStatusProfile.text = mUserModel.status
                    userNameProfile.text = mUserModel.name
                    profilePhone.text = getString(R.string.info_is_not_enabled)
                    profileBio.text = getString(R.string.info_is_not_enabled)
                    profileBio.setTypeface(null, Typeface.ITALIC)
                    profileBio.setTextColor(APP_ACTIVITY_MAIN.resources.getColor(R.color.statusColor))
                    profilePhone.setTypeface(null, Typeface.ITALIC)
                    profilePhone.setTextColor(APP_ACTIVITY_MAIN.resources.getColor(R.color.statusColor))
                    tintClipCall.visibility = View.VISIBLE
                    tintClipVideo.visibility = View.VISIBLE
                    tintClipSearch.visibility = View.VISIBLE
                    tintClipMore.visibility = View.VISIBLE
                    lockAccount.visibility = View.VISIBLE
                    callProfile.isEnabled = false
                    videoCallProfile.isEnabled = false
                    searchProfile.isEnabled = false
                    moreProfile.isEnabled = false
                }
                else if(mUserModel.privateAccount && checkFriend){
                    lockAccount.visibility = View.VISIBLE
                    userStatusProfile.text = mUserModel.status
                    userNameProfile.text = mUserModel.name
                    profilePhone.text = mUserModel.phone
                    profileBio.text = mUserModel.bio
                }
            }


            mViewModel.checkForBlockMe(mUserModel) { blockedMe ->
                if (blockedMe) {
                    userStatusProfile.text = mUserModel.status
                    userNameProfile.text = mUserModel.name
                    profilePhone.text = getString(R.string.info_is_not_enabled)
                    profileBio.text = getString(R.string.info_is_not_enabled)
                    profileBio.setTypeface(null, Typeface.ITALIC)
                    profileBio.setTextColor(APP_ACTIVITY_MAIN.resources.getColor(R.color.statusColor))
                    profilePhone.setTypeface(null, Typeface.ITALIC)
                    profilePhone.setTextColor(APP_ACTIVITY_MAIN.resources.getColor(R.color.statusColor))

                    tintClipCall.visibility = View.VISIBLE
                    tintClipVideo.visibility = View.VISIBLE
                    tintClipSearch.visibility = View.VISIBLE
                    tintClipMore.visibility = View.VISIBLE
                    tintClipBox.visibility = View.VISIBLE

                    callProfile.isEnabled = false
                    videoCallProfile.isEnabled = false
                    searchProfile.isEnabled = false
                    moreProfile.isEnabled = false
                    addInFriend.isEnabled = false
                    addInBlocklist.isEnabled = false
                    addInFavorites.isEnabled = false
                    addReport.isEnabled = false
                } else {
                    if (!mUserModel.privateAccount) {
                        userStatusProfile.text = mUserModel.status
                        userNameProfile.text = mUserModel.name
                        profilePhone.text = mUserModel.phone
                        profileBio.text = mUserModel.bio
                    }
                }
            }
            mViewModel.checkBlockOrNot(mUserModel) {
                checkBlock = it
                if (checkBlock) {
                    addInBlockLabel.text = getString(R.string.remove_from_black_list)
                } else {
                    addInBlockLabel.text = getString(R.string.add_blocklist)
                }
            }

            userPhotoProfile.setOnClickListener {
                val bundle = Bundle()
                val model = SavedPhotoModel(image_url = mUserModel.iconUrl)
                val list = mutableListOf(model)
                bundle.putParcelableArrayList(KEY_CHAT_IMAGE, list as ArrayList<SavedPhotoModel>)
                bundle.putInt(KEY_POSITION, 0)
                APP_ACTIVITY_MAIN.mNavController.navigate(
                    R.id.action_userProfileFragment_to_imageFullScreenFragment,
                    bundle
                )
            }

            addInFriend.setOnClickListener {
                if (mUserModel.privateAccount) {
                    if(checkFriend){
                        mViewModel.deleteFromFriends(mUserModel) {
                            addInFriendLabel.text = getString(R.string.add_in_friend)
                            checkFriend = false
                        }
                    }
                    else{
                        mViewModel.sendNotificationPrivateAccountApp(mUserModel) {
                            showSnackBarLong(it, getString(R.string.add_in_friend_private_account), R.color.colorGreen)
                        }
                    }
                } else {
                    if (checkFriend) {
                        mViewModel.deleteFromFriends(mUserModel) {
                            addInFriendLabel.text = getString(R.string.add_in_friend)
                            checkFriend = false
                        }
                    } else {
                        mViewModel.addInFriends(mUserModel) {
                            mViewModel.sendNotification(
                                mUserModel,
                                USER.nickname,
                                USER.name + " " + getString(R.string.add_in_friend_notification)
                            )
                            addInFriendLabel.text = getString(R.string.remove_from_friends)
                            checkFriend = true
                        }
                    }
                }
            }
            addInBlocklist.setOnClickListener {
                if (checkBlock) {
                    mViewModel.deleteFromBlock(mUserModel) {
                        addInBlockLabel.text = getString(R.string.add_blocklist)
                    }
                } else {
                    DialogApp.create(DialogSettings.PROFILE_DIALOG) { typeAdd, text ->
                        when (typeAdd) {
                            ADD_TO_BLACK_LIST -> {
                                mViewModel.addInBlock(text, mUserModel) {
                                    addInBlockLabel.text =
                                        getString(R.string.remove_from_black_list)
                                    mViewModel.deleteMeFromFriends(mUserModel)
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}