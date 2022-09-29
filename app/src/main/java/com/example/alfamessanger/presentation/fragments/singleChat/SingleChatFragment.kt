package com.example.alfamessanger.presentation.fragments.singleChat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.devlomi.record_view.OnRecordListener
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.SingleChatFragmentBinding
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.BottomSheetApp
import com.example.alfamessanger.presentation.PopupWindowApp
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.AppViewFactory
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.BottomSheetSettings
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.single_chat_fragment.*
import java.util.ArrayList

@AndroidEntryPoint
class SingleChatFragment : Fragment() {

    private val mViewModel: SingleChatViewModel by viewModels()
    private var binding: SingleChatFragmentBinding? = null
    private val mBinding get() = binding!!
    private lateinit var mUserModel: UserModel
    private var flag = false
    private var flagAnim = false
    private var mCountMessages = 15
    private var mIsScrolling = false
    private var smoothScrollToPosition = true
    private var editTextMessage = false
    private lateinit var uriGraffiti: Uri
    private lateinit var mMessageKey: String
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var animZoom: Animation
    private lateinit var animOut: Animation
    private lateinit var animShowLoad: Animation
    private lateinit var animHideLoad: Animation

    private lateinit var mObserverLoadMessage: Observer<MessageModel>
    private lateinit var mObserverDeleteMessage: Observer<MessageModel>
    private lateinit var editMessageModel: MessageView

    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SingleChatFragmentBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        hideKeyBoard()
        toolbarTools(ToolbarSettings.CHAT_SETTINGS, null)
        initFields()
        initRecyclerView()
        initObservers()
    }

    private fun initRecyclerView() {
        swipeLayout = mBinding.chatSwipeLayout
        mRecyclerView = mBinding.recyclerMessages
        mAdapter = SingleChatAdapter() {
            editMessageModel = it
            mBinding.enterMessage.setText(it.text)
            editTextMessage = true
        }
        layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)

        // чтобы сообщения отображались снизу
        layoutManager.stackFromEnd = true

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false

        mAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mRecyclerView.adapter = mAdapter

        mRecyclerView.visibility = View.INVISIBLE
        mBinding.dialogIsEmpty.visibility = View.GONE
        mBinding.progressLoadMessages.visibility = View.VISIBLE

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && layoutManager.findFirstVisibleItemPosition() <= 3) {
                    if (!flagAnim) {
                        flagAnim = true
                        mBinding.scrollRecycler.startAnimation(animZoom)
                        mBinding.scrollRecycler.visibility = View.VISIBLE
                    }
                    updateData()
                }
                if (layoutManager.findLastVisibleItemPosition() == mAdapter.itemCount - 1) {
                    if (flagAnim) {
                        flagAnim = false
                        mBinding.scrollRecycler.startAnimation(animOut)
                        mBinding.scrollRecycler.visibility = View.GONE
                    }
                }
            }
        })

        swipeLayout.setOnRefreshListener {
            updateData()
        }
    }

    private fun initObservers() {
        mObserverDeleteMessage = Observer { message ->
            mAdapter.removeFromList(AppViewFactory.getView(message))
        }
        mViewModel.messageDelete.observe(APP_ACTIVITY_MAIN, mObserverDeleteMessage)
        mViewModel.observDeleteMessage()
        mObserverLoadMessage = Observer { message ->
            mRecyclerView.visibility = View.VISIBLE
            mBinding.progressLoadMessages.visibility = View.GONE
            mBinding.dialogIsEmpty.visibility = View.GONE
            mAdapter.addToList(
                mUserModel,
                AppViewFactory.getView(message),
                smoothScrollToPosition
            ) {
                if (smoothScrollToPosition) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
                swipeLayout.isRefreshing = false
            }
        }
        mViewModel.message.observe(APP_ACTIVITY_MAIN, mObserverLoadMessage)
        mViewModel.getMessage(mUserModel.uid.toString(), mCountMessages) {
            mBinding.progressLoadMessages.visibility = View.GONE
            mBinding.dialogIsEmpty.visibility = View.VISIBLE
        }
    }

    private fun updateData() {
        smoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mViewModel.getMessage(mUserModel.uid.toString(), mCountMessages) {}
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    private fun initFields() {
        APP_ACTIVITY_MAIN.setSupportActionBar(APP_ACTIVITY_MAIN.findViewById(R.id.toolbar))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowTitleEnabled(false)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.window.statusBarColor =
            ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)

        initRecordVoiceMessage()

        mUserModel = arguments?.getSerializable(KEY_USER_CHAT) as UserModel
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0

        initCheckBlocked()
        mViewModel.getUserModel(mUserModel.uid.toString()) { model ->
            mUserModel = model
        }

        APP_ACTIVITY_MAIN.toolbar.user_photo_chat.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(KEY_PROFILE, mUserModel)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_singleChatFragment_to_userProfileFragment,
                bundle
            )
        }

        mBinding.apply {
            APP_ACTIVITY_MAIN.toolbar.user_name_chat.text = mUserModel.name
            getStatusUser(mUserModel.uid.toString()) {
                APP_ACTIVITY_MAIN.toolbar.status_chat.text = it
            }
            animZoom =
                AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.send_message_btn_anim)
            animOut =
                AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.send_message_btn_anim_out)
            animHideLoad = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.toolbar_out)
            animShowLoad = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.toolbar_in)

            if (mUserModel.iconUrl != "") {
                APP_ACTIVITY_MAIN.toolbar.user_photo_chat.downloadAndSetImage(
                    true,
                    mUserModel.iconUrl
                )
            } else {
                APP_ACTIVITY_MAIN.toolbar.user_photo_chat.setImageDrawable(
                    APP_ACTIVITY_MAIN.resources.getDrawable(
                        R.drawable.default_user,
                        APP_ACTIVITY_MAIN.theme
                    )
                )
            }
            enterMessage.addTextChangedListener {
                if (it?.isNotEmpty() == true) {
                    if (!flag) {
                        flag = true
                        sendMessage.startAnimation(animZoom)
                        sendMessage.visibility = View.VISIBLE
                        recordButton.startAnimation(animOut)
                        recordButton.visibility = View.INVISIBLE
                    }
                } else {
                    flag = false
                    sendMessage.startAnimation(animOut)
                    sendMessage.visibility = View.GONE
                    recordButton.startAnimation(animZoom)
                    recordButton.visibility = View.VISIBLE
                }
            }
            addInChat.setOnClickListener {
                //CropImageBuilder.cropImage(this@SingleChatFragment, 300, 300, false)
                BottomSheetApp.create(BottomSheetSettings.CHAT_BOTTOM_SHEET) { typeAdd ->
                    when (typeAdd) {
                        CAMERA -> {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, PICK_IMAGE_CAMERA)
                        }
                        GALLERY -> {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            startActivityForResult(intent, PICK_IMAGE_GALLERY)
                        }
                        FILE -> {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
                        }
                    }
                }
            }
        }

        sticker.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(KEY_UID, mUserModel.uid)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_singleChatFragment_to_graffitiFragment,
                bundle
            )
        }

        sendMessage.setOnClickListener {
            smoothScrollToPosition = true
            if (editTextMessage) {
                mViewModel.editMessage(
                    mUserModel.uid.toString(),
                    editMessageModel.id,
                    mBinding.enterMessage.text.toString()
                ) {
                    editMessageModel.text = enter_message.text.toString()
                    mAdapter.changeItemInList(editMessageModel)
                    enter_message.text.clear()
                }
                editTextMessage = false
            } else {
                mViewModel.sendMessage(
                    mUserModel,
                    enter_message.text.toString(),
                    mUserModel.uid.toString(),
                    TYPE_TEXT
                ) {
                    mViewModel.sendNotification(
                        mUserModel,
                        USER.name,
                        enter_message.text.toString()
                    )
                    enter_message.text.clear()
                }
            }
        }
        scrollRecycler.setOnClickListener {
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            flagAnim = false
            mBinding.scrollRecycler.startAnimation(animOut)
            mBinding.scrollRecycler.visibility = View.GONE
        }

    }

    private fun initRecordVoiceMessage() {
        mBinding.apply {
            recordButton.setRecordView(recordView)
            recordView.setSoundEnabled(false)
            recordView.setTrashIconColor(Color.parseColor("#FF2A2A"))
            recordView.setSlideToCancelTextColor(Color.parseColor("#838383"))
            recordView.setSlideToCancelArrowColor(Color.parseColor("#838383"))
            recordView.setOnRecordListener(object : OnRecordListener {
                override fun onStart() {
                    recordButton.setBackgroundResource(R.drawable.send_voice_style)
                    addInChat.visibility = View.INVISIBLE
                    sticker.visibility = View.INVISIBLE
                    enterMessage.visibility = View.INVISIBLE
                    if (AppPermissions.checkPermission(RECORD_AUDIO_PERMISSION)) {
                        mViewModel.startRecordVoiceMessage(mUserModel.uid.toString())
                    }
                }

                override fun onCancel() {
                    recordButton.visibility = View.INVISIBLE
                    mViewModel.cancelRecord()
                }

                override fun onFinish(recordTime: Long, limitReached: Boolean) {
                    addInChat.visibility = View.VISIBLE
                    sticker.visibility = View.VISIBLE
                    enterMessage.visibility = View.VISIBLE
                    recordButton.setBackgroundResource(R.drawable.ic_microphone)
                    showLoadBar(true, getString(R.string.voice_loading))
                    mViewModel.stopRecordVoiceMessage(mUserModel) {
                        showLoadBar(false, null)
                        mViewModel.sendNotification(
                            mUserModel,
                            USER.name,
                            getString(R.string.gs)
                        )
                    }
                }

                override fun onLessThanSecond() {
                    addInChat.visibility = View.VISIBLE
                    sticker.visibility = View.VISIBLE
                    enterMessage.visibility = View.VISIBLE
                    recordButton.setBackgroundResource(R.drawable.ic_microphone)
                    recordButton.visibility = View.VISIBLE
                    mViewModel.cancelRecord()
                }

            })

            recordView.setOnBasketAnimationEndListener {
                addInChat.visibility = View.VISIBLE
                sticker.visibility = View.VISIBLE
                enterMessage.visibility = View.VISIBLE
                recordButton.setBackgroundResource(R.drawable.ic_microphone)
                recordButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.message.removeObserver(mObserverLoadMessage)
        mViewModel.messageDelete.removeObserver(mObserverDeleteMessage)
        mViewModel.removeObservers()
        mViewModel.releaseRecorder()
    }

    private fun initCheckBlocked() {
        mViewModel.checkForBlock(mUserModel) { blocked ->
            if (blocked) {
                mBinding.blockedTittle.visibility = View.VISIBLE
                mBinding.enterMessage.isEnabled = false
                mBinding.addInChat.isEnabled = false
                mBinding.sendMessage.isEnabled = false
                mBinding.recordButton.isEnabled = false
            }
        }

    }

    override fun onPause() {
        super.onPause()
        PopupWindowApp.dismissPopup()
        APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        sendGraffiti()
    }

    private fun sendGraffiti() {
        val uriGraffitiString = AppPreferences.getGraffiti()
        if (uriGraffitiString != "" || uriGraffitiString.isNotEmpty()) {
            uriGraffiti = Uri.parse(uriGraffitiString)
            val bitmapImage = BitmapFactory.decodeStream(
                APP_ACTIVITY_MAIN.contentResolver.openInputStream(uriGraffiti),
                null,
                null
            )
            showLoadBar(true, getString(R.string.image_loading))
            mViewModel.createMessageKey(mUserModel.uid.toString()) { messageKey ->
                val path = STORAGE_REFERENCE.child(FOLDER_MESSAGES_IMAGE).child(messageKey)
                if (bitmapImage != null) {
                    mViewModel.loadPic(
                        mUserModel,
                        messageKey,
                        mUserModel.uid.toString(),
                        uriGraffiti,
                        path,
                        bitmapImage
                    ) {
                        showLoadBar(false, null)
                        mViewModel.sendNotification(
                            mUserModel,
                            USER.name,
                            getString(R.string.pic_notification)
                        )
                        AppPreferences.setGraffiti(Uri.parse(""))
                    }
                }
            }
        }
    }


    private fun showLoadBar(status: Boolean, tittle: String?) {
        mBinding.apply {
            if (status) {
                loadText.text = tittle ?: ""
                loadContainer.startAnimation(animShowLoad)
                loadContainer.visibility = View.VISIBLE
            } else {
                mBinding.loadContainer.startAnimation(animHideLoad)
                mBinding.loadContainer.visibility = View.GONE
            }
        }
    }

    @SuppressLint("Range", "Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            mViewModel.createMessageKey(mUserModel.uid.toString()) { messageKey ->
                mMessageKey = messageKey
                when (requestCode) {
                    PICK_IMAGE_GALLERY -> {
                        val uri = data.data
                        val bitmapImage = createBitmapForSize(uri)
                        val path = STORAGE_REFERENCE.child(FOLDER_MESSAGES_IMAGE).child(messageKey)
                        if (bitmapImage != null && uri != null) {
                            showLoadBar(true, getString(R.string.image_loading))
                            mViewModel.loadPic(
                                mUserModel,
                                messageKey,
                                mUserModel.uid.toString(),
                                uri,
                                path,
                                bitmapImage
                            ) {
                                showLoadBar(false, null)
                                mViewModel.sendNotification(
                                    mUserModel,
                                    USER.name,
                                    getString(R.string.pic_notification)
                                )
                            }
                        }
                        smoothScrollToPosition = true
                    }
                    PICK_IMAGE_CAMERA -> {
                        val uri = data.data
                        val bitmapImage = createBitmapForSize(uri)
                        val path = STORAGE_REFERENCE.child(FOLDER_MESSAGES_IMAGE).child(messageKey)
                        if (bitmapImage != null && uri != null) {
                            showLoadBar(true, getString(R.string.image_loading))
                            mViewModel.loadPic(
                                mUserModel,
                                messageKey,
                                mUserModel.uid.toString(),
                                uri,
                                path,
                                bitmapImage
                            ) {
                                showLoadBar(false, null)
                                mViewModel.sendNotification(
                                    mUserModel,
                                    USER.name,
                                    getString(R.string.pic_notification)
                                )
                            }
                        }
                        smoothScrollToPosition = true
                    }
                    PICK_FILE_REQUEST_CODE -> {
                        val uri = data.data
                        if (uri != null) {
                            checkSizeOfFile(uri) { name, size ->
                                showLoadBar(true, getString(R.string.file_loading))
                                mViewModel.sendFile(
                                    mUserModel,
                                    mUserModel.uid.toString(),
                                    uri,
                                    messageKey,
                                    name,
                                    size.toDouble()
                                ) {
                                    showLoadBar(false, null)
                                    mViewModel.sendNotification(
                                        mUserModel,
                                        USER.name,
                                        getString(R.string.file_notification),
                                    )
                                }
                            }
                        }
                    }
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
                R.id.action_singleChatFragment_to_imageFullScreenFragment,
                bundle
            )
        }
    }
}