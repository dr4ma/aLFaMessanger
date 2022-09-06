package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.data.firebase.FileStorageRequests
import com.example.alfamessanger.data.firebase.SingleChatRequests
import com.example.alfamessanger.data.firebase.UserModelRequests
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.domain.usecases.SingleChatUseCase
import com.example.alfamessanger.presentation.PopupWindowApp
import com.example.alfamessanger.presentation.fragments.singleChat.SingleChatFragment
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.PopupTypesOperationChat
import kotlinx.android.synthetic.main.messages_image_layout.view.*


class HolderImageMessage(private val model : UserModel, view : View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val singleChatRepository: SingleChatRepository = SingleChatRequests()
    private val fileStorageRepository: FileStorageRepository = FileStorageRequests()
    private val userModelRepository: GetUserModelRepository = UserModelRequests()
    private val singleChatUseCase: SingleChatUseCase =
        SingleChatUseCase(singleChatRepository, fileStorageRepository, userModelRepository)

    private val blocCameImage : ConstraintLayout = view.bloc_came_image
    private val blocReceivedImage : ConstraintLayout = view.bloc_received_image
    private val cameImage : ImageView = view.chat_came_image_c
    private val receivedImage : ImageView = view.chat_received_image
    private val cameImageTime : TextView = view.chat_came_image_time
    private val receivedImageTime : TextView = view.chat_received_image_time
    private val mainImage : ConstraintLayout = view.mainImage
    private val deletingImage : ConstraintLayout = view.deletingMassageImg

    override fun drawMessage(view: MessageView) {
        mainImage.animation =
            AnimationUtils.loadAnimation(itemView.context, R.anim.recycler_view)
        if (view.from == UID) {
            resizeImageForChat(view.height, view.width, receivedImage)
            blocReceivedImage.visibility = View.VISIBLE
            blocCameImage.visibility = View.GONE
            receivedImage.downloadAndSetImage(false, view.file_url)
            receivedImageTime.text = view.time_stamp.toString().asTime()
        } else {
            resizeImageForChat(view.height, view.width, cameImage)
            blocCameImage.visibility = View.VISIBLE
            blocReceivedImage.visibility = View.GONE
            cameImage.downloadAndSetImage(false, view.file_url)
            cameImageTime.text = view.time_stamp.toString().asTime()
        }
    }
    override fun onAttach(view: MessageView) {
        if (view.from == UID) {
            blocReceivedImage.setOnClickListener {
                SingleChatFragment.openFullScreenImage(view.file_url)
            }
            blocReceivedImage.setOnLongClickListener {
                blocReceivedImage.setBackgroundResource(R.drawable.image_received_message_style_highlight)
                blocReceivedImage.setPadding(5)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_IMAGE, RECEIVED_MESSAGE, blocReceivedImage)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
                PopupWindowApp.create(POPUP_NORMAL_CHAT, it){ typeOperation ->
                    when(typeOperation){
                        PopupTypesOperationChat.DELETE -> {
                            deletingImage.visibility = View.VISIBLE
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            singleChatUseCase.deleteMessageForAll(model, view.id){
                                singleChatUseCase.deleteFileFromStorage(view.file_url, view.id)
                                deletingImage.visibility = View.GONE
                            }
                        }
                        PopupTypesOperationChat.COPY -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            copyTextClipBoard(view.file_url)
                            blocReceivedImage.setBackgroundResource(R.drawable.image_received_message_style)
                            showSnackBar(blocReceivedImage, APP_ACTIVITY_MAIN.getString(R.string.url_copy), R.color.colorGreen)
                        }
                    }

                }
                true
            }
        }
        else{
            blocCameImage.setOnClickListener {
                SingleChatFragment.openFullScreenImage(view.file_url)
            }
            blocCameImage.setOnLongClickListener {
                blocCameImage.setBackgroundResource(R.drawable.came_image_messages_style_highlight)
                blocCameImage.setPadding(5)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_IMAGE, CAME_MESSAGE, blocCameImage)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
                PopupWindowApp.create(POPUP_NORMAL_CHAT, it){ typeOperation ->
                    when(typeOperation){
                        PopupTypesOperationChat.DELETE -> {
                            deletingImage.visibility = View.VISIBLE
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            singleChatUseCase.deleteMessageForAll(model, view.id){
                                singleChatUseCase.deleteFileFromStorage(view.file_url, view.id)
                                deletingImage.visibility = View.GONE
                            }
                        }
                        PopupTypesOperationChat.COPY -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            copyTextClipBoard(view.file_url)
                            blocCameImage.setBackgroundResource(R.drawable.came_image_messages_style)
                            showSnackBar(blocCameImage, APP_ACTIVITY_MAIN.getString(R.string.url_copy), R.color.colorGreen)
                        }
                    }

                }
                true
            }
        }
    }

    override fun onDetach() {
        blocReceivedImage.setOnClickListener(null)
        blocCameImage.setOnClickListener(null)
        blocCameImage.setOnLongClickListener(null)
        blocReceivedImage.setOnLongClickListener(null)
    }

}