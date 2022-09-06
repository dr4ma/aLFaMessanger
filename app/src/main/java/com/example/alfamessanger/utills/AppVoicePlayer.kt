package com.example.alfamessanger.utills

import android.media.MediaPlayer
import android.util.Log
import android.widget.TextView
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import rm.com.audiowave.AudioWaveView
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

class AppVoicePlayer(private val mFile : File) {

    private lateinit var mMediaPlayer: MediaPlayer

    fun play(fileUrl: String, view :MessageView, waveView: AudioWaveView, duration: TextView, function: () -> Unit) {
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay(view, waveView, duration) {
                function()
            }
        } else {
            mFile.createNewFile()
            getFileFromStorage(mFile, fileUrl) {
                startPlay(view, waveView, duration) {
                    function()
                }
            }
        }
    }

    fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
        val path = STORAGE_REFERENCE.storage.getReferenceFromUrl(fileUrl)
        path.getFile(mFile)
            .addOnSuccessListener {
                function()
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Get file error: " + error.message.toString())
            }
    }

    private fun startPlay(view :MessageView, waveView: AudioWaveView, duration: TextView, function: () -> Unit) {
        try {
            mMediaPlayer.apply {
                setDataSource(mFile.absolutePath)
                prepare()
                start()
                Timer.initTimer(view.duration.toLong(), view.duration.toLong(), waveView, duration)
                Timer.startTimer()
                setOnCompletionListener {
                    stopPlayer() {
                        function()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Start player error: " + e.message.toString())
        }
    }

    fun stopPlayer(function: () -> Unit) {
        try {
            Timer.stopTimer()
            mMediaPlayer.stop()
            mMediaPlayer.reset()
            function()
        } catch (e: Exception) {
            Log.e(TAG, "Stop player error: " + e.message.toString())
        }
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init(){
        mMediaPlayer = MediaPlayer()
    }
}
