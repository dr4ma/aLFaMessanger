package com.example.alfamessanger.presentation.fragments.channel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.ChannelFragmentBinding
import com.example.alfamessanger.domain.models.*
import com.example.alfamessanger.presentation.BottomSheetApp
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.BottomSheetSettings
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChannelFragment : Fragment() {

    private val mViewModel: ChannelViewModel by viewModels()

    private var binding: ChannelFragmentBinding? = null
    private val mBinding get() = binding!!

    private lateinit var mChannelModel: ChannelModel
    private lateinit var mObserverLoadFeed: Observer<FeedModel>
    private lateinit var mRecyclerView: RecyclerView

    @Inject
    lateinit var mAdapter: ChannelAdapter

    private lateinit var iconUrl: Uri
    private lateinit var fileUrl: Uri
    private var iconUriDatabase: String = ""
    private var fileUriDatabase: String = ""
    private var nameFile: String = ""
    private var heightImage: Int = 0
    private var sizeFile: Double = 0.0
    private var admin: Boolean = false
    private var sub: Boolean = false
    private var countSubs = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChannelFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
        initToolbarInfo()
        initBoxAdminVisible()
        initObservers()
    }

    private fun initBoxAdminVisible() {
        if (mChannelModel.adminUid == USER.uid) {
            mBinding.mainBox.visibility = View.VISIBLE
            admin = true
        } else {
            mBinding.mainBox.visibility = View.GONE
            admin = false
        }
    }

    private fun initObservers() {
        mObserverLoadFeed = Observer { feed ->
            mAdapter.addToList(feed)
        }
        mViewModel.feed.observe(APP_ACTIVITY_MAIN, mObserverLoadFeed)
        mViewModel.getFeed(mChannelModel.channelId) {
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initToolbarInfo() {
        if (mChannelModel.iconUrl != "") {
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.downloadAndSetImage(
                true,
                mChannelModel.iconUrl
            )
        } else {
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        APP_ACTIVITY_MAIN.user_name_chat.text = mChannelModel.name
        APP_ACTIVITY_MAIN.status_chat.text =
            getString(R.string.subs) + " " + mChannelModel.countSubs
    }

    private fun initFields() {
        toolbarTools(ToolbarSettings.CHAT_SETTINGS, null)
        setHasOptionsMenu(true)
        hideKeyBoard()
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowTitleEnabled(false)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.window.statusBarColor =
            ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)

        mChannelModel = arguments?.getSerializable(KEY_CHANNEL) as ChannelModel
        mRecyclerView = mBinding.recyclerChannel
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        layoutManager.stackFromEnd = true
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mRecyclerView.adapter = mAdapter
        mAdapter.clearList()

        mBinding.addCreateNews.setOnClickListener {
            BottomSheetApp.create(BottomSheetSettings.ADMIN_CREATE_NEWS_BOTTOM_SHEET) { type ->
                when (type) {
                    ADD_PHOTO -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_GALLERY)
                    }
                    ADD_FILE -> {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "*/*"
                        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
                    }
                }
            }
        }

        mBinding.deleteImage.setOnClickListener {
            iconUriDatabase = ""
            heightImage = 0
            mBinding.card.visibility = View.GONE
            mBinding.addImageCreateNews.setImageResource(R.drawable.box_content_style)
        }
        mBinding.deleteFile.setOnClickListener {
            fileUriDatabase = ""
            sizeFile = 0.0
            mBinding.linearFile.visibility = View.GONE
        }

        mBinding.createNewsChannel.setOnClickListener {
            hideKeyBoard()
            if (mBinding.textNewsChannel.text.isNotEmpty()) {
                mViewModel.sendNewsChannel(
                    sizeFile,
                    heightImage,
                    mChannelModel.channelId,
                    mBinding.textNewsChannel.text.toString(),
                    iconUriDatabase,
                    fileUriDatabase,
                    nameFile
                ) { model ->
                    loadFileAndImageFeed(model)
                    mBinding.linearFile.visibility = View.GONE
                    mBinding.card.visibility = View.GONE
                    mBinding.addImageCreateNews.setImageResource(R.drawable.box_content_style)
                    iconUriDatabase = ""
                    fileUriDatabase = ""
                    sizeFile = 0.0
                    heightImage = 0
                    mBinding.fileName.text = getString(R.string.file_was_added)
                    mBinding.textNewsChannel.text.clear()
                }
            } else {
                showToast(getString(R.string.enter_text))
            }
        }

        mViewModel.checkSubOrNot(mChannelModel.channelId){
            sub = it
        }
    }

    private fun loadFileAndImageFeed(model: FeedModel) {
        if (fileUriDatabase != "") {
            val fileName: String = UUID.randomUUID().toString()
            val path = STORAGE_REFERENCE.child(FOLDER_CHANNEL_FILE).child(fileName)
            mViewModel.uploadChannelNewsFile(
                mChannelModel.channelId,
                model,
                fileUrl,
                path
            )
        }
        if (iconUriDatabase != "") {
            val fileName: String = UUID.randomUUID().toString()
            val path = STORAGE_REFERENCE.child(FOLDER_CHANNEL_IMAGE).child(fileName)
            mViewModel.uploadChannelNewsImage(
                mChannelModel.channelId,
                model,
                iconUrl,
                path
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.user_channel_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_channel -> {
                if (admin) {
                    BottomSheetApp.create(BottomSheetSettings.ADMIN_CHANNEL_MAIN_BOTTOM_SHEET) {
                        when (it) {
                            SETTINGS -> {

                            }
                            SUBSCRIBERS -> {
                                goToSubs(mChannelModel)
                            }
                            DELETE_CHANNEL -> {
                                mViewModel.deleteChannel(mChannelModel.channelId){
                                    APP_ACTIVITY_MAIN.onBackPressed()
                                }
                            }
                        }
                    }
                } else {
                    if (sub) {
                        BottomSheetApp.create(BottomSheetSettings.SUBSCRIBER_CHANNEL_MAIN_BOTTOM_SHEET) {
                            when (it) {
                                CHANNEL_PAGE -> {

                                }
                                SUBSCRIBERS_ -> {
                                    goToSubs(mChannelModel)
                                }
                                UNSUBSCRIBE_CHANNEL -> {
                                    mViewModel.unSubscribeChannel(mChannelModel, USER){
                                        APP_ACTIVITY_MAIN.onBackPressed()
                                        APP_ACTIVITY_MAIN.status_chat.text =
                                            getString(R.string.subs) + " " + (mChannelModel.countSubs - 1).toString()
                                    }
                                }
                            }
                        }
                    } else {
                        BottomSheetApp.create(BottomSheetSettings.WATCHER_CHANNEL_MAIN_BOTTOM_SHEET) {
                            when (it) {
                                CHANNEL_PAGE -> {

                                }
                                SUBSCRIBERS_ -> {
                                    goToSubs(mChannelModel)
                                }
                                SUBSCRIBE_CHANNEL -> {
                                    mViewModel.subscribeChannel(mChannelModel, USER) {
                                        APP_ACTIVITY_MAIN.status_chat.text =
                                            getString(R.string.subs) + " " + (mChannelModel.countSubs + 1).toString()
                                        sub = true
                                    }
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

   private fun goToSubs(model: ChannelModel) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_SUBS, model)
        APP_ACTIVITY_MAIN.mNavController.navigate(
            R.id.action_channelFragment_to_subscribersFragment,
            bundle
        )
    }

    override fun onStop() {
        super.onStop()
        mViewModel.feed.removeObserver(mObserverLoadFeed)
        mViewModel.removeObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_GALLERY -> {
                    iconUrl = data.data!!
                    val bitmapImage = createBitmapForSize(iconUrl)
                    if (bitmapImage != null) {
                        heightImage = bitmapImage.height
                    }
                    mBinding.addImageCreateNews.downloadAndSetImage(false, iconUrl.toString())
                    iconUriDatabase = iconUrl.toString()
                    mBinding.card.visibility = View.VISIBLE
                }
                PICK_FILE_REQUEST_CODE -> {
                    fileUrl = data.data!!
                    checkSizeOfFile(fileUrl) { name, size ->
                        sizeFile = size.toDouble()
                        fileUriDatabase = fileUrl.toString()
                        mBinding.fileName.text = name
                        nameFile = name
                    }
                    mBinding.linearFile.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun openFullScreenImage(url: String) {
            val bundle = Bundle()
            val model = SavedPhotoModel(image_url = url)
            val list = mutableListOf(model)
            bundle.putParcelableArrayList(KEY_CHAT_IMAGE, list as ArrayList<SavedPhotoModel>)
            bundle.putInt(KEY_POSITION, 0)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_channelFragment_to_imageFullScreenFragment,
                bundle
            )
        }

    }

}