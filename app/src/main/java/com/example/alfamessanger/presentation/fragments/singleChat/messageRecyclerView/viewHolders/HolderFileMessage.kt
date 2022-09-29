package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders

import android.view.View
import android.widget.ProgressBar
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
import kotlinx.android.synthetic.main.messages_file_layout.view.*

class HolderFileMessage(private val model : UserModel, view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val singleChatRepository: SingleChatRepository = SingleChatRequests()
    private val fileStorageRepository: FileStorageRepository = FileStorageRequests()
    private val userModelRepository: GetUserModelRepository = UserModelRequests()
    private val singleChatUseCase: SingleChatUseCase =
        SingleChatUseCase(singleChatRepository, fileStorageRepository, userModelRepository)

    private val blocCameFile: ConstraintLayout = view.bloc_came_messages_file
    private val blocReceivedFile: ConstraintLayout = view.bloc_received_messages_file
    private val fileNameReceived: TextView = view.file_received_name
    private val fileNameCame: TextView = view.file_came_name
    private val timeFileReceived: TextView = view.received_message_file_time
    private val timeFileCame: TextView = view.came_message_time_file
    private val receivedSize: TextView = view.received_file_size
    private val cameSize: TextView = view.came_file_size
    private val receivedProgress : ProgressBar = view.load_progress_received_file
    private val cameProgress : ProgressBar = view.load_progress_came_file
    private val deletingImage : ConstraintLayout = view.deletingMassageFile


    override fun drawMessage(view: MessageView) {
        if (view.from == UID) {
            blocReceivedFile.visibility = View.VISIBLE
            blocCameFile.visibility = View.INVISIBLE
            fileNameReceived.text = view.text
            timeFileReceived.text = view.time_stamp.toString().asTime()
            receivedSize.text = convertBytesToUpper(view.size)

        } else {
            blocCameFile.visibility = View.VISIBLE
            blocReceivedFile.visibility = View.INVISIBLE
            fileNameCame.text = view.text
            timeFileCame.text = view.time_stamp.toString().asTime()
            cameSize.text = convertBytesToUpper(view.size)
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == UID) {
            blocReceivedFile.setOnClickListener {
                receivedProgress.visibility = View.VISIBLE
                singleChatUseCase.downloadFile(view.file_url, view.text){
                    receivedProgress.visibility = View.GONE
                }
            }
            blocReceivedFile.setOnLongClickListener {
                blocReceivedFile.setBackgroundResource(R.drawable.received_messages_style_highlight)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_FILE, RECEIVED_MESSAGE, blocReceivedFile)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
                PopupWindowApp.create(POPUP_NORMAL_CHAT, it) { typeOperation ->
                    when (typeOperation) {
                        PopupTypesOperationChat.DELETE -> {
                            deletingImage.visibility = View.VISIBLE
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            singleChatUseCase.deleteMessageForAll(model, view.id) {
                                singleChatUseCase.deleteFileFromStorage(view.file_url, view.id)
                                deletingImage.visibility = View.GONE
                            }
                        }
                        PopupTypesOperationChat.COPY -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            copyTextClipBoard(view.file_url)
                            blocReceivedFile.setBackgroundResource(R.drawable.came_image_messages_style)
                            showSnackBar(
                                blocReceivedFile,
                                APP_ACTIVITY_MAIN.getString(R.string.url_copy),
                                R.color.colorGreen
                            )
                        }
                    }
                }
                true
            }
        }
        else{
            blocCameFile.setOnClickListener {
                cameProgress.visibility = View.VISIBLE
                singleChatUseCase.downloadFile(view.file_url, view.text){
                    cameProgress.visibility = View.GONE
                }
            }
            blocCameFile.setOnLongClickListener {
                blocCameFile.setBackgroundResource(R.drawable.received_messages_style_highlight)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_FILE, CAME_MESSAGE, blocCameFile)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
                PopupWindowApp.create(POPUP_NORMAL_CHAT, it) { typeOperation ->
                    when (typeOperation) {
                        PopupTypesOperationChat.DELETE -> {
                            deletingImage.visibility = View.VISIBLE
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            singleChatUseCase.deleteMessageForAll(model, view.id) {
                                singleChatUseCase.deleteFileFromStorage(view.file_url, view.id)
                                deletingImage.visibility = View.GONE
                            }
                        }
                        PopupTypesOperationChat.COPY -> {
                            APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE
                            copyTextClipBoard(view.file_url)
                            blocCameFile.setBackgroundResource(R.drawable.came_image_messages_style)
                            showSnackBar(
                                blocCameFile,
                                APP_ACTIVITY_MAIN.getString(R.string.url_copy),
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
        blocCameFile.setOnClickListener(null)
        blocReceivedFile.setOnClickListener(null)
        blocCameFile.setOnLongClickListener(null)
        blocReceivedFile.setOnLongClickListener(null)
    }
}