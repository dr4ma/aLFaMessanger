package com.example.alfamessanger.utills.listeners

import android.widget.SeekBar

class SeekBarListener(val onSuccess:(Int) -> Unit) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        onSuccess(p1)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }
}