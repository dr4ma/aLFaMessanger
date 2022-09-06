package com.example.alfamessanger.utills

import android.os.CountDownTimer
import android.widget.TextView
import rm.com.audiowave.AudioWaveView

object Timer {

    private lateinit var timer: CountDownTimer

    fun initTimer(
        duration: Long,
        interval: Long,
        progressBar: AudioWaveView,
        durationText: TextView
    ) {
        timer =
            object : CountDownTimer(duration, interval / 50) {
                override fun onTick(millisUntilFinished: Long) {
                    durationText.text = toTime(millisUntilFinished.toInt())
                    if(progressBar.progress + 1 > 100){
                        progressBar.progress = 100f
                    }
                    else{
                        progressBar.progress += 2
                    }
                }

                override fun onFinish() {
                    progressBar.progress = 0F
                }
            }
    }
    fun startTimer(){
        timer.start()
    }
    fun stopTimer(){
        timer.cancel()
    }
}