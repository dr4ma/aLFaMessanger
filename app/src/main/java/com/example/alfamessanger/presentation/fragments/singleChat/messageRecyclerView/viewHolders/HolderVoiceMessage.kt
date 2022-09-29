package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders

import android.view.View
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.messages_voice_layout.view.*
import rm.com.audiowave.AudioWaveView
import java.io.File


class HolderVoiceMessage(private val model: UserModel, view: View) : RecyclerView.ViewHolder(view),
    MessageHolder {

    private lateinit var mAppVoicePlayer: AppVoicePlayer

    private val singleChatRepository: SingleChatRepository = SingleChatRequests()
    private val fileStorageRepository: FileStorageRepository = FileStorageRequests()
    private val userModelRepository: GetUserModelRepository = UserModelRequests()
    private val singleChatUseCase: SingleChatUseCase =
        SingleChatUseCase(singleChatRepository, fileStorageRepository, userModelRepository)

    private val blocCameVoice: ConstraintLayout = view.bloc_came_messages_voice
    private val blocReceivedVoice: ConstraintLayout = view.bloc_received_messages_voice
    private val playReceived: ImageView = view.play_voice_received
    private val stopReceived: ImageView = view.stop_voice_received
    private val playCame: ImageView = view.play_voice_came
    private val stopCame: ImageView = view.stop_voice_came
    private val timeVoiceReceived: TextView = view.chat_received_message_voice_time
    private val timeVoiceCame: TextView = view.chat_came_message_time_voice
    private val receivedDuration: TextView = view.received_voice_duration
    private val cameDuration: TextView = view.came_voice_duration

    private val waveReceived: AudioWaveView = view.waveReceived
    private val waveCame: AudioWaveView = view.waveCame
    private val deletingImage : ConstraintLayout = view.deletingMassage


    override fun drawMessage(view: MessageView) {
        val mFile = File(APP_ACTIVITY_MAIN.filesDir, view.id)
        mAppVoicePlayer = AppVoicePlayer(mFile)
        if (!mFile.exists() || mFile.length().toInt() == 0 || !mFile.isFile) {
            mFile.createNewFile()
            mAppVoicePlayer.getFileFromStorage(mFile, view.file_url) {
                waveReceived.setRawData(mFile.readBytes())
            }
        }
        if (view.from == UID) {
            blocReceivedVoice.visibility = View.VISIBLE
            blocCameVoice.visibility = View.INVISIBLE
            waveCame.visibility = View.INVISIBLE
            waveReceived.setRawData(mFile.readBytes())
            waveReceived.visibility = View.VISIBLE
            timeVoiceReceived.text = view.time_stamp.toString().asTime()
            receivedDuration.text = toTime(view.duration)
            when {
                view.duration < 5000 -> {
                    waveReceived.layoutParams.width = 200
                }
                view.duration < 8000 -> {
                    waveReceived.layoutParams.width = 300
                }
                else -> {
                    waveReceived.layoutParams.width = 450
                }
            }
        } else {
            waveCame.visibility = View.VISIBLE
            waveReceived.visibility = View.INVISIBLE
            blocCameVoice.visibility = View.VISIBLE
            blocReceivedVoice.visibility = View.INVISIBLE
            waveCame.setRawData(mFile.readBytes())
            timeVoiceCame.text = view.time_stamp.toString().asTime()
            cameDuration.text = toTime(view.duration)
            when {
                view.duration < 5000 -> {
                    waveCame.layoutParams.width = 200
                }
                view.duration < 8000 -> {
                    waveCame.layoutParams.width = 300
                }
                else -> {
                    waveCame.layoutParams.width = 450
                }
            }
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if (view.from == UID) {
            playReceived.setOnClickListener {

                playReceived.visibility = View.INVISIBLE
                stopReceived.visibility = View.VISIBLE
                stopReceived.setOnClickListener {
                    stop() {
                        playReceived.visibility = View.VISIBLE
                        stopReceived.visibility = View.INVISIBLE
                        waveReceived.progress = 0f
                        receivedDuration.text = toTime(view.duration)
                    }
                }
                play(view, waveReceived, receivedDuration) {
                    playReceived.visibility = View.VISIBLE
                    stopReceived.visibility = View.INVISIBLE
                    receivedDuration.text = toTime(view.duration)
                }
            }
        } else {
            playCame.setOnClickListener {
                playCame.visibility = View.INVISIBLE
                stopCame.visibility = View.VISIBLE
                stopCame.setOnClickListener {
                    stop() {
                        playCame.visibility = View.VISIBLE
                        stopCame.visibility = View.INVISIBLE
                        waveCame.progress = 0f
                        cameDuration.text = toTime(view.duration)
                    }
                }
                play(view, waveCame, cameDuration) {
                    playCame.visibility = View.VISIBLE
                    stopCame.visibility = View.INVISIBLE
                    cameDuration.text = toTime(view.duration)
                }
            }
        }
        if (view.from == UID) {
            blocReceivedVoice.setOnLongClickListener {
                blocReceivedVoice.setBackgroundResource(R.drawable.received_messages_style_highlight)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_VOICE, RECEIVED_MESSAGE, blocReceivedVoice)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor =
                    ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
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
                            blocCameVoice.setBackgroundResource(R.drawable.came_image_messages_style)
                            showSnackBar(
                                blocCameVoice,
                                APP_ACTIVITY_MAIN.getString(R.string.url_copy),
                                R.color.colorGreen
                            )
                        }
                    }
                }
                true
            }
        } else {
            blocCameVoice.setOnLongClickListener {
                blocCameVoice.setBackgroundResource(R.drawable.received_messages_style_highlight)
                APP_ACTIVITY_MAIN.initTintApp(TYPE_VOICE, CAME_MESSAGE, blocCameVoice)
                APP_ACTIVITY_MAIN.tintApp.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.window.statusBarColor =
                    ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.colorStatusBarTint)
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
                            blocCameVoice.setBackgroundResource(R.drawable.came_image_messages_style)
                            showSnackBar(
                                blocCameVoice,
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

    private fun play(
        view: MessageView,
        waveView: AudioWaveView,
        duration: TextView,
        function: () -> Unit
    ) {
        mAppVoicePlayer.play(view.file_url, view, waveView, duration) {
            function()
        }
    }

    private fun stop(function: () -> Unit) {
        mAppVoicePlayer.stopPlayer {
            function()
        }
    }

    override fun onDetach() {
        playReceived.setOnClickListener(null)
        playCame.setOnClickListener(null)
        blocCameVoice.setOnLongClickListener(null)
        blocReceivedVoice.setOnLongClickListener(null)
        mAppVoicePlayer.release()
    }
}