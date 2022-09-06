package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.PopupTypesOperationChat
import kotlinx.android.synthetic.main.messages_text_layout.view.*

class HolderTextMessages(
    private val model: UserModel,
    view: View,
    val function: (view: MessageView) -> Unit
) : RecyclerView.ViewHolder(view), MessageHolder {

    private val singleChatRepository: SingleChatRepository = SingleChatRequests()
    private val fileStorageRepository: FileStorageRepository = FileStorageRequests()
    private val userModelRepository: GetUserModelRepository = UserModelRequests()
    private val singleChatUseCase: SingleChatUseCase =
        SingleChatUseCase(singleChatRepository, fileStorageRepository, userModelRepository)

    private val blocReceivedMessages: ConstraintLayout = view.bloc_received_messages
    private val receivedMessage: TextView = view.chat_received_message
    private val receivedTime: TextView = view.chat_came_message_time

    private val blocCameMessages: ConstraintLayout = view.bloc_came_messages
    private val blocMain: ConstraintLayout = view.main
    private val cameMessage: TextView = view.chat_came_message
    private val cameTime: TextView = view.chat_received_message_time

    override fun drawMessage(view: MessageView) {
        blocMain.animation =
            AnimationUtils.loadAnimation(itemView.context, R.anim.recycler_view)
        if (view.from == UID) {
            blocReceivedMessages.visibility = View.VISIBLE
            blocCameMessages.visibility = View.GONE
            receivedMessage.text = view.text
            receivedTime.text = view.time_stamp.toString().asTime()
        } else {
            blocReceivedMessages.visibility = View.GONE
            blocCameMessages.visibility = View.VISIBLE
            cameMessage.text = view.text
            cameTime.text = view.time_stamp.toString().asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == UID) {
            blocReceivedMessages.setOnLongClickListener {
                blocReceivedMessages.setBackgroundResource(R.drawable.received_messages_style_highlight)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_TEXT, RECEIVED_MESSAGE, blocReceivedMessages)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor =
                    ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
                PopupWindowApp.create(POPUP_TEXT_CHAT, it) { typeOperation ->
                    when (typeOperation) {
                        PopupTypesOperationChat.DELETE -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            singleChatUseCase.deleteMessageForAll(model, view.id) {}
                        }
                        PopupTypesOperationChat.COPY -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            copyTextClipBoard(view.text)
                            blocReceivedMessages.setBackgroundResource(R.drawable.received_messages_style)
                            showSnackBar(
                                blocReceivedMessages,
                                APP_ACTIVITY_MAIN.getString(R.string.text_copy),
                                R.color.colorGreen
                            )
                        }
                        PopupTypesOperationChat.EDIT -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            blocReceivedMessages.setBackgroundResource(R.drawable.received_messages_style)
                            function(view)
                        }
                    }

                }
                true
            }
        } else {
            blocCameMessages.setOnLongClickListener {
                blocCameMessages.setBackgroundResource(R.drawable.came_messages_style_highlight)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_TEXT, CAME_MESSAGE, blocCameMessages)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor =
                    ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
                PopupWindowApp.create(POPUP_NORMAL_CHAT, it) { typeOperation ->
                    when (typeOperation) {
                        PopupTypesOperationChat.DELETE -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            singleChatUseCase.deleteMessageForAll(model, view.id) {}
                        }
                        PopupTypesOperationChat.COPY -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            copyTextClipBoard(view.text)
                            blocCameMessages.setBackgroundResource(R.drawable.came_messages_style)
                            showSnackBar(
                                blocReceivedMessages,
                                "Текст скопирован",
                                R.color.colorGreen
                            )
                        }
                    }
                }
                true
            }
        }
    }

    override fun onDetach() {
        blocReceivedMessages.setOnLongClickListener(null)
        blocCameMessages.setOnLongClickListener(null)
    }

}